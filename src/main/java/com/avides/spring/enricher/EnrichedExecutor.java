package com.avides.spring.enricher;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Martin Schumacher
 */
@Aspect
public class EnrichedExecutor
{
    private static Map<Class<?>, List<Enricher<?>>> mappedEnrichers = new HashMap<>();

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

    @AfterReturning(pointcut = "@annotation(com.avides.spring.enricher.Enriched)", returning = "retVal")
    public Object enrich(Object retVal)
    {
        for (StackTraceElement element : Thread.currentThread().getStackTrace())
        {
            if (element.getClassName().equals(Enriching.class.getName()) && element.getMethodName().equals("suppressEnriching"))
            {
                return retVal;
            }
        }
        return doEnrich(retVal, null);
    }

    @SuppressWarnings("unchecked")
    public static Object doEnrich(Object retVal, List<Class<? extends Enricher<?>>> suppressedEnrichers)
    {
        if (retVal != null)
        {
            if (List.class.isAssignableFrom(retVal.getClass()))
            {
                @SuppressWarnings("rawtypes") List list = (List) retVal;
                if (!list.isEmpty())
                {
                    Object anyValue = list.stream().filter(value -> value != null).findFirst().orElse(null);
                    List<Enricher<?>> enrichers = findEnrichers(anyValue, suppressedEnrichers);
                    if (enrichers != null)
                    {
                        for (Enricher<?> enricher : enrichers)
                        {
                            list = enricher.enrich(list);
                        }
                    }
                }
                return list;
            }
            else
            {
                List<Enricher<?>> enrichers = findEnrichers(retVal, suppressedEnrichers);
                if (enrichers != null)
                {
                    for (Enricher<?> enricher : enrichers)
                    {
                        retVal = enricher.enrich(retVal);
                    }
                }
                return retVal;
            }
        }
        return null;
    }

    private static List<Enricher<?>> findEnrichers(Object value, List<Class<? extends Enricher<?>>> suppressedEnrichers)
    {
        if (value != null)
        {
            for (Entry<Class<?>, List<Enricher<?>>> entry : mappedEnrichers.entrySet())
            {
                if (entry.getKey().isAssignableFrom(value.getClass()))
                {
                    List<Enricher<?>> enrichers = entry.getValue();
                    List<Enricher<?>> filtered = new ArrayList<>(enrichers);
                    if ((suppressedEnrichers != null) && !suppressedEnrichers.isEmpty())
                    {
                        filtered = enrichers.stream().filter(enricher -> !suppressedEnrichers.contains(enricher.getClass())).collect(toList());
                    }
                    return filtered;
                }
            }
        }
        return null;
    }
}