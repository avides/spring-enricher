package com.avides.spring.enricher.enricher;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
public interface Enricher<T>
{
    boolean applies(Object object);

    void enrich(final T object);

    void enrich(final List<T> objects);

    Class<?> getApplyingType();

    public static <T> T suppressEnriching(Supplier<T> callable)
    {
        return callable.get();
    }
}
