package com.avides.spring.enricher;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;

/**
 * @author Martin Schumacher
 */
public abstract class AbstractEnricher<T> implements Enricher<T>
{
    private static final Logger log = getLogger(AbstractEnricher.class);

    private Class<T> type;

    protected abstract T doEnrich(T object);

    public AbstractEnricher(Class<T> type)
    {
        this.type = type;
    }

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
    @SuppressWarnings("unchecked")
    public T enrich(Object object)
    {
        return doEnrich((T) object);
    }

    @Override
    public List<T> enrich(List<T> objects)
    {
        for (T object : objects)
        {
            object = enrich(object);
        }
        return objects;
    }

    public T enrichWith(T object, Object data)
    {
        String setterName = "set" + data.getClass().getSimpleName();
        Method setter = findMethod(object.getClass(), setterName, data.getClass());
        if (setter != null)
        {
            invokeMethod(setter, object, data);
        }
        else
        {
            String message = "no setter '" + setterName + "' found for " + object.getClass().getName();
            log.error(message);
            throw new RuntimeException(message);
        }
        return object;
    }

    public List<T> enrichWith(List<T> objects, Object data)
    {
        for (T object : objects)
        {
            object = enrichWith(object, data);
        }
        return objects;
    }
}