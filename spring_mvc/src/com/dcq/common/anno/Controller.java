package com.dcq.common.anno;

import java.lang.annotation.*;

/**
 * @日期: 2019-01-12 22:45
 * @作者: 杜超群
 * @描述: ElementType.TYPE 作用在类上，Retention 表示注解的生命周期,Documented是否可转换为api jar文件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
