package com.avides.spring.enricher;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.avides.spring.enricher.AbstractEnricher;

public class AbstractEnricherTest
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
        assertThat(anyEnricher.applies(new AnyObject())).isTrue();
        assertThat(anyEnricher.applies(new String())).isFalse();
    }

    @Test(expected = RuntimeException.class)
    public void testEnrichWithWithError()
    {
        AnyObject anyObject = new AnyObject();
        anyEnricher.enrichWith(anyObject, "anyString");
    }

    @Test(expected = RuntimeException.class)
    public void testEnrichNotFoundSetter()
    {
        AnyObject anyObject = new AnyObject();
        anyEnricher.enrichWith(anyObject, Long.valueOf(1234L));
    }

    private class AnyEnricher extends AbstractEnricher<AnyObject>
    {
        public AnyEnricher()
        {
            super(AnyObject.class);
        }

        @Override
        public AnyObject doEnrich(AnyObject object)
        {
            // nothing to do here
            return null;
        }
    }

    private class AnyObject
    {
        @SuppressWarnings("unused")
        private void setString(String string)
        {
            // nothing to do here
        }
    }
}