package com.avides.spring.enricher.enricher;

import java.util.List;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 *
 * @param <T>
 *            the generic type of the enricher / the type of the objects to
 *            enrich
 */
public abstract class AbstractEnricher<T> implements Enricher<T>
{
    private Class<T> type;

    public AbstractEnricher(Class<T> type)
    {
        this.type = type;
    }

    protected abstract void doEnrich(final T object);

    @Override
    public Class<?> getApplyingType()
    {
        return type;
    }

    @Override
    public boolean applies(Object object)
    {
        return object != null ? type.isAssignableFrom(object.getClass()) : false;
    }

    @Override
    public void enrich(T object)
    {
        if (object != null)
        {
            doEnrich(object);
        }
    }

    @Override
    public void enrich(final List<T> objects)
    {
        for (T object : objects)
        {
            enrich(object);
        }
    }
}