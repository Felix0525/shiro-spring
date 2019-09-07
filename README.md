## 前言

在实际系统应用中，普遍存在这样的一种业务场景，需要实现用户对要访问的资源进行动态权限校验。
譬如，在某平台的商家系统中，存在商家、品牌、商品等业务资源。它们之间的关系为：一个商家可以拥有多个品牌，一个品牌下可以拥有多个商品。


![text](https://github.com/Felix0525/assets/blob/master/20190907-0001.png?raw=true)


一个商家用户可以拥有多个账户，每个账户拥有不同级别的权限。
例如，小王负责商家A下的所有资源的运营工作，小张负责品牌A和品牌A下所有商品的运营工作。而小李负责品牌B

![](https://github.com/Felix0525/assets/blob/master/20190907-0002.png?raw=true)


Shiro本身提供了RequiresAuthentication、RequiresPermissions和RequiresRoles等注解用于实现静态权限认证，
但不适合对于这种细粒度的动态资源的权限认证校验。基于以上描述，这篇文章就是补充了一种对细粒度动态资源的访问权限校验。

## 大概的设计思路

* 1.新增一个自定义注解Permitable，用于将资源转换为shiro的权限表示字符串(支持SpEL表达式)
* 2.新增加一个AOP切面，用于将自定义注解标注的方法和Shiro权限校验关联起来
* 3.校验当前用户是否拥有足够的权限去访问受保护的资源

## 编码实现

* 1、新建PermissionResolver接口

```java
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * 资源权限解析器
 *
 * @author wuyue
 * @since 1.0, 2019-09-07
 */
public interface PermissionResolver {

    /**
     * 解析资源
     *
     * @return 资源的权限表示字符串
     */
    String resolve();

    /**
     * 批量解析资源
     */
    static List<String> resolve(List<PermissionResolver> list) {
        return Optional.ofNullable(list).map(obj -> obj.stream().map(PermissionResolver::resolve).collect(toList()))
                .orElse(Collections.emptyList());
    }

}
```

* 2、新增业务资源实体类，并实现PermissionResolver接口，此处以商品资源为例，例如新建Product.java

```java
import com.wuyue.shiro.shiro.PermissionResolver;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product implements PermissionResolver {

    @Override
    public String resolve() {
        return merchantId + ":" + brandId + ":" + id;
    }

    @Id
    @GenericGenerator(name = "idGen", strategy = "uuid")
    @GeneratedValue(generator = "idGen")
    private String id;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "name")
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
```

* 3、新增自定义注解Permitable

```java
import java.lang.annotation.*;

/**
 * 自定义细粒度权限校验注解，配合SpEL表达式使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permitable {

    /**
     * 前置校验资源权限表达式
     *
     * @return 资源的权限字符串表示(如“字节跳动”下的“抖音”可以表达为BYTE_DANCE:TIK_TOK)
     */
    String pre() default "";

    /**
     * 后置校验资源权限表达式
     *
     * @return
     */
    String post() default "";

}
```

* 4、新增权限校验切面
```java
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 静态自定义权限认证切面
 */
@Slf4j
public class PermitAdvisor extends StaticMethodMatcherPointcutAdvisor {

    private static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES =
            new Class[] {
                    Permitable.class
            };

    public PermitAdvisor(SpelExpressionParser parser) {
        // 构造一个通知，当方法上有加入Permitable注解时，会触发此通知执行权限校验
        MethodInterceptor advice = mi -> {
            Method method = mi.getMethod();
            Object targetObject = mi.getThis();
            Object[] args = mi.getArguments();
            Permitable permitable = method.getAnnotation(Permitable.class);
            // 前置权限认证
            checkPermission(parser, permitable.pre(), method, args, targetObject, null);
            Object proceed = mi.proceed();
            // 后置权限认证
            checkPermission(parser, permitable.post(), method, args, targetObject, proceed);
            return proceed;
        };
        setAdvice(advice);
    }

    /**
     * 匹配加了Permitable注解的方法，用于通知权限校验
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method m = method;

        if (isAuthzAnnotationPresent(m)) {
            return true;
        }
        return false;
    }

    private boolean isAuthzAnnotationPresent(Method method) {
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if ( a != null ) {
                return true;
            }
        }
        return false;
    }

    /**
     * 动态权限认证
     */
    private void checkPermission(SpelExpressionParser parser, String expr,
                                 Method method, Object[] args, Object target, Object result){

        if (StringUtils.isBlank(expr)){
            return;
        }

        // 解析SpEL表达式，获得资源的权限表示字符串
        Object resources = parser.parseExpression(expr)
                .getValue(createEvaluationContext(method, args, target, result), Object.class);

        // 调用Shiro进行权限校验
        if (resources instanceof String) {
            SecurityUtils.getSubject().checkPermission((String) resources);
        } else if (resources instanceof List){
            List<Object> list = (List) resources;
            list.stream().map(obj -> (String) obj).forEach(SecurityUtils.getSubject()::checkPermission);
        }
    }

    /**
     * 构造SpEL表达式上下文
     */
    private EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Object result) {
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
                target, method, args, new DefaultParameterNameDiscoverer());
        evaluationContext.setVariable("result", result);
        try {
            evaluationContext.registerFunction("resolve", PermissionResolver.class.getMethod("resolve", List.class));
        } catch (NoSuchMethodException e) {
            log.error("Get method error:", e);
        }
        return evaluationContext;
    }

}
```

* 5、实现对用户的授权

```java
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Map<String, Object> principal = (Map<String, Object>) principals.getPrimaryPrincipal();
        String accountId = (String) principal.get("accountId");

        // 拥有的商家资源权限
        List<AccountMerchantLink> merchantLinks = accountService.findMerchantLinks(accountId);
        Set<String> merchantPermissions = merchantLinks.stream().map(AccountMerchantLink::getMerchantId).collect(toSet());
        SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();
        authzInfo.addStringPermissions(merchantPermissions);

        // 拥有的品牌资源权限
        List<AccountBrandLink> brandLinks = accountService.findBrandLinks(accountId);
        Set<String> brandPermissions = brandLinks.stream().map(link -> link.getMerchantId() + ":" + link.getBrandId()).collect(toSet());
        authzInfo.addStringPermissions(brandPermissions);

        return authzInfo;
    }
```

* 6、自定义注解的应用

  6.1、根据id获取商家信息
```java
    @Permitable(pre = "#id")
    @Override
    public Optional<Merchant> findById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        return merchantDao.findById(id);
    }
```

  6.2、根据id获取商品信息
```java
    @Permitable(post = "#result?.get().resolve()")
    @Override
    public Optional<Product> findById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        return productDao.findById(id);
    }
```

  6.3、查找品牌下的商品列表
```java
    @Permitable(post = "#resolve(#result)")
    @Override
    public List<Product> findByBrandId(String brandId) {
        if (StringUtils.isBlank(brandId)) {
            return Collections.emptyList();
        }
        return productDao.findByBrandId(brandId);
    }
```

* 7、测试

7.1、按照上面描述的业务场景，准备3个用户数据

![](https://github.com/Felix0525/assets/blob/master/20190907-00010.png?raw=true)

7.2、使用小王登录后测试

![](https://github.com/Felix0525/assets/blob/master/20190907-0003.png?raw=true)


7.2.1、获取商家信息（拥有权限）

![](https://github.com/Felix0525/assets/blob/master/20190907-0004.png?raw=true)

7.2.2、获取商品信息（拥有权限）

![](https://github.com/Felix0525/assets/blob/master/20190907-0005.png?raw=true)

7.3、使用小李登录后测试

![](https://github.com/Felix0525/assets/blob/master/20190907-0006.png?raw=true)


7.3.1、获取商家信息（权限不足）

![](https://github.com/Felix0525/assets/blob/master/20190907-0007.png?raw=true)
7.3.2、获取商品信息（权限不足）

![](https://github.com/Felix0525/assets/blob/master/20190907-0008.png?raw=true)
7.3.3、获取商品信息（拥有权限）

![](https://github.com/Felix0525/assets/blob/master/20190907-0009.png?raw=true)


7.4、小结
     
从上面的接口测试截图中可以看出，此方案符合我们设计之初要实现的业务场景。
