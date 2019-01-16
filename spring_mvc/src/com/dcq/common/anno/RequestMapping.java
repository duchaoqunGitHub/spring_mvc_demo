package com.dcq.common.anno;

import java.lang.annotation.*;

/**
 * @日期: 2019-01-12 22:46
 * @作者: 杜超群
 * @描述:
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";

}
