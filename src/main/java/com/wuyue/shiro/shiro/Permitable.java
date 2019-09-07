package com.wuyue.shiro.shiro;

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
