package com.dcq.common.anno;

import java.lang.annotation.*;

/**
 * @日期: 2019-01-12 22:57
 * @作者: 杜超群
 * @描述:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
