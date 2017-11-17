package com.avides.spring.enricher.configuration;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
@Retention(RUNTIME)
@Target(TYPE)
@Import(EnrichingConfiguration.class)
public @interface EnableEnriching
{
}