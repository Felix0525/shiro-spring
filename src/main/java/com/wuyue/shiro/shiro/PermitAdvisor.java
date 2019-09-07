package com.wuyue.shiro.shiro;

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
