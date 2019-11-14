package com.avides.spring.enricher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.avides.spring.enricher.enricher.Enricher;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
public class EnricherService
{
    private Map<Class<?>, List<Enricher<?>>> mappedEnrichers = new HashMap<>();

    @Autowired
    public void setEnrichers(List<Enricher<?>> enrichers)
    {
        for (Enricher<?> enricher : enrichers)
        {
            List<Enricher<?>> enricherList = mappedEnrichers.get(enricher.getApplyingType());
            if (enricherList == null)
            {
                enricherList = new ArrayList<>();
                mappedEnrichers.put(enricher.getApplyingType(), enricherList);
            }
            enricherList.add(enricher);
        }
    }

    public void enrich(Object value, Class<?>... applyingEnrichers)
    {
        enrich(value, applyingEnrichers, new Class<?>[0]);
    }

    public <T> void enrich(T value, Class<?>[] applyingEnrichers, Class<?>[] ignoreEnrichers)
    {
        if (value != null)
        {
            if (List.class.isAssignableFrom(value.getClass()))
            {
                @SuppressWarnings("rawtypes") List list = (List) value;
                if (!list.isEmpty())
                {
                    for (Object item : list)
                    {
                        enrich(item, applyingEnrichers, ignoreEnrichers);
                    }
                }
            }
            else
            {
                for (Enricher<T> enricher : findEnrichers(value, applyingEnrichers, ignoreEnrichers))
                {
                    enricher.enrich(value);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<Enricher<T>> findEnrichers(T value, Class<?>[] applyingEnrichers, Class<?>[] ignoreEnrichers)
    {
        List<Enricher<?>> allTypeApplyingEnrichers = new ArrayList<>();
        for (Entry<Class<?>, List<Enricher<?>>> entry : mappedEnrichers.entrySet())
        {
            if (entry.getKey().isAssignableFrom(value.getClass()))
            {
                allTypeApplyingEnrichers.addAll(entry.getValue());
            }
        }
        List<Enricher<T>> allApplyingEnrichers = new ArrayList<>();
        for (Enricher<?> typeApplyingEnricher : allTypeApplyingEnrichers)
        {
            if (((applyingEnrichers.length == 0) || isAnyAssignableFrom(applyingEnrichers, typeApplyingEnricher.getClass()))
                && !isAnyAssignableFrom(ignoreEnrichers, typeApplyingEnricher.getClass()))
            {
                allApplyingEnrichers.add((Enricher<T>) typeApplyingEnricher);
            }
        }
        return allApplyingEnrichers;
    }

    private boolean isAnyAssignableFrom(Class<?>[] types, Class<?> type)
    {
        for (Class<?> currentType : types)
        {
            if (currentType.isAssignableFrom(type))
            {
                return true;
            }
        }
        return false;
    }
}
