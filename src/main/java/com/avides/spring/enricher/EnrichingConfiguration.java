package com.avides.spring.enricher;

import org.springframework.context.annotation.Bean;

/**
 * @author Martin Schumacher
 */
public class EnrichingConfiguration
{
    @Bean
    public EnrichedExecutor enrichedExecutor()
    {
        return new EnrichedExecutor();
    }
}