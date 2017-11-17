package com.avides.spring.enricher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avides.spring.enricher.service.EnrichedExecutor;
import com.avides.spring.enricher.service.EnricherService;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
@Configuration
public class EnrichingConfiguration
{
    @Bean
    public EnricherService enricherService()
    {
        return new EnricherService();
    }

    @Bean
    public EnrichedExecutor enrichedExecutor()
    {
        return new EnrichedExecutor();
    }
}