package com.avides.spring.enricher.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Enriched
{
    Class<?>[] value() default
    {};

    Class<?>[] ignore() default
    {};
}