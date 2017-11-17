package com.avides.spring.enricher.testsupport;

import com.avides.spring.enricher.enricher.AbstractEnricher;

public abstract class EnrichingTestSupport
{
    protected class AnyEnricher extends AbstractEnricher<AnyObject>
    {
        public AnyEnricher()
        {
            super(AnyObject.class);
        }

        @Override
        public void doEnrich(AnyObject object)
        {
            object.setValue("anyString - " + object.getSuffix());
        }
    }

    protected class AnyEnricher2 extends AbstractEnricher<AnyObject2>
    {
        public AnyEnricher2()
        {
            super(AnyObject2.class);
        }

        @Override
        public void doEnrich(AnyObject2 object)
        {
            // nothing to do here
        }
    }

    protected class AnyObject
    {
        private String value;
        private String suffix;

        public AnyObject(String suffix)
        {
            this.suffix = suffix;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public String getSuffix()
        {
            return suffix;
        }
    }

    protected class AnyObject2
    {
    }
}