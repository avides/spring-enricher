package com.avides.spring.enricher;

import static com.avides.spring.enricher.EnrichedExecutor.doEnrich;
import static java.util.Arrays.asList;

import java.util.function.Supplier;

public abstract class Enriching
{
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> T suppressEnriching(Supplier<T> callable, Class<? extends Enricher<?>>... suppressedEnrichers)
    {
        if (suppressedEnrichers.length == 0)
        {
            return callable.get();
        }
        return (T) doEnrich(callable.get(), asList(suppressedEnrichers));
    }
}