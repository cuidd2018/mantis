package com.u002.core.extension;

import java.lang.annotation.*;

/**
 * @author maijunsheng
 * @version 创建时间：2013-5-28
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {

    Scope scope() default Scope.PROTOTYPE;

}
