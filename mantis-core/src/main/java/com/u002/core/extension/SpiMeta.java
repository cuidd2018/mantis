package com.u002.core.extension;

import java.lang.annotation.*;

/**
 * @author amber
 * @version 创建时间：2013-5-28
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SpiMeta {
    String name() default "";
}
