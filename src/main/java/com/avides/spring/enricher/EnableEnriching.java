package com.avides.spring.enricher;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author Martin Schumacher
 */
@Retention(RUNTIME)
@Target(TYPE)
@Import(EnrichingConfiguration.class)
public @interface EnableEnriching
{
}