package com.avides.spring.enricher.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.avides.spring.enricher.testsupport.EnrichingTestSupport;

public class EnricherServiceTest extends EnrichingTestSupport
{
    private EnricherService enricherService = new EnricherService();

    @Before
    public void setUp()
    {
        enricherService.setEnrichers(asList(new AnyEnricher(), new AnyEnricher2()));
    }

    @Test
    public void testEnrich()
    {
        AnyObject anyObject = new AnyObject("anySuffix");
        assertThat(anyObject.getValue()).isNull();
        enricherService.enrich(anyObject);
        assertThat(anyObject.getValue()).isEqualTo("anyString - anySuffix");

        enricherService.enrich(null);
    }

    @Test
    public void testEnrichList()
    {
        AnyObject anyObject1 = new AnyObject("anySuffix1");
        AnyObject anyObject2 = new AnyObject("anySuffix2");
        assertThat(anyObject1.getValue()).isNull();
        assertThat(anyObject2.getValue()).isNull();
        enricherService.enrich(asList(anyObject1, null, anyObject2));
        assertThat(anyObject1.getValue()).isEqualTo("anyString - anySuffix1");
        assertThat(anyObject2.getValue()).isEqualTo("anyString - anySuffix2");
    }

    @Test
    public void testEnrichWithGivenApplyingEnricherClasses1()
    {
        AnyObject anyObject = new AnyObject("anySuffix");
        assertThat(anyObject.getValue()).isNull();
        enricherService.enrich(anyObject, AnyEnricher.class);
        assertThat(anyObject.getValue()).isEqualTo("anyString - anySuffix");

        enricherService.enrich(null, AnyEnricher.class);
    }

    @Test
    public void testEnrichWithGivenApplyingEnricherClasses2()
    {
        AnyObject anyObject = new AnyObject("anySuffix");
        assertThat(anyObject.getValue()).isNull();
        enricherService.enrich(anyObject, AnyEnricher2.class);
        assertThat(anyObject.getValue()).isNull();

        enricherService.enrich(null, AnyEnricher2.class);
    }

    @Test
    public void testEnrichWithGivenIgnoreEnricherClasses()
    {
        AnyObject anyObject = new AnyObject("anySuffix");
        assertThat(anyObject.getValue()).isNull();
        enricherService.enrich(anyObject, new Class[0], new Class[]
        { AnyEnricher.class });
        assertThat(anyObject.getValue()).isNull();

        enricherService.enrich(null, AnyEnricher.class);
    }

    @Test
    public void testEnrichWithGivenApplyingAndIgnoreEnricherClasses()
    {
        AnyObject anyObject = new AnyObject("anySuffix");
        assertThat(anyObject.getValue()).isNull();
        enricherService.enrich(anyObject, new Class[]
        { AnyEnricher.class }, new Class[]
        { AnyEnricher.class });
        assertThat(anyObject.getValue()).isNull();

        enricherService.enrich(null, AnyEnricher.class);
    }
}
