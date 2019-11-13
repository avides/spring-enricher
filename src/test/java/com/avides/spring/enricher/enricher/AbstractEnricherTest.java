package com.avides.spring.enricher.enricher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.avides.spring.enricher.testsupport.EnrichingTestSupport;

public class AbstractEnricherTest extends EnrichingTestSupport
{
    private AnyEnricher anyEnricher;

    @Before
    public void setUp()
    {
        anyEnricher = new AnyEnricher();
    }

    @Test
    public void testApplies()
    {
        assertThat(anyEnricher.applies(new AnyObject("suffix1"))).isTrue();
        assertThat(anyEnricher.applies(new String())).isFalse();
        assertThat(anyEnricher.applies(null)).isFalse();
    }

    @Test
    public void testGetApplyingType()
    {
        assertThat(anyEnricher.getApplyingType()).isSameAs(AnyObject.class);
    }

    @Test
    public void testEnrich()
    {
        AnyObject object = new AnyObject("suffix1");
        assertThat(object.getValue()).isNull();
        anyEnricher.enrich(object);
        assertThat(object.getValue()).isEqualTo("anyString - suffix1");

        anyEnricher.enrich((AnyObject) null);
    }

    @Test
    public void testEnrichList()
    {
        AnyObject object1 = new AnyObject("suffix1");
        AnyObject object2 = new AnyObject("suffix2");
        assertThat(object1.getValue()).isNull();
        assertThat(object2.getValue()).isNull();
        anyEnricher.enrich(Arrays.asList(object1, null, object2));
        assertThat(object1.getValue()).isEqualTo("anyString - suffix1");
        assertThat(object2.getValue()).isEqualTo("anyString - suffix2");
    }
}
