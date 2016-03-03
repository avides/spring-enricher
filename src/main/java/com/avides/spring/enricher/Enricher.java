package com.avides.spring.enricher;

import java.util.List;

/**
 * @author Martin Schumacher
 */
public interface Enricher<T>
{
    boolean applies(Object object);

    T enrich(Object object);

    List<T> enrich(List<T> objects);

    Class<?> getApplyingType();
}