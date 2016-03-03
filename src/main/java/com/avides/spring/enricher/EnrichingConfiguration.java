package com.avides.spring.enricher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Martin Schumacher
 */
@Configuration
public class EnrichingConfiguration
{
    @Bean
    public EnrichedExecutor enrichedExecutor()
    {
        return new EnrichedExecutor();
    }
}