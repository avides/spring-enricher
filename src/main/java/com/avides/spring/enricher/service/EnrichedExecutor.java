package com.avides.spring.enricher.service;

import java.lang.reflect.Method;
import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.avides.spring.enricher.annotation.Enriched;
import com.avides.spring.enricher.enricher.Enricher;

/**
 * @author Martin Schumacher
 * @since 1.0.0.RELEASE
 */
@Aspect
public class EnrichedExecutor
{
    @Autowired
    private EnricherService enricherService;

    private Function<ProceedingJoinPoint, Enriched> enrichedAnnotationSupplier = (pjp) -> getAnnotation(pjp);

    @Around("@annotation(com.avides.spring.enricher.annotation.Enriched)")
    public Object enrich(ProceedingJoinPoint pjp) throws Throwable
    {
        Object retVal = pjp.proceed();
        if (isEnrichingSuppressed())
        {
            return retVal;
        }
        Enriched enriched = enrichedAnnotationSupplier.apply(pjp);
        enricherService.enrich(retVal, enriched.value(), enriched.ignore());
        return retVal;
    }

    private static boolean isEnrichingSuppressed()
    {
        for (StackTraceElement element : Thread.currentThread().getStackTrace())
        {
            if (element.getClassName().equals(Enricher.class.getName()) && "suppressEnriching".equals(element.getMethodName()))
            {
                return true;
            }
        }
        return false;
    }

    private static Enriched getAnnotation(ProceedingJoinPoint pjp)
    {
        try
        {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            if (method.getDeclaringClass().isInterface())
            {
                method = pjp.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            }
            return method.getAnnotation(Enriched.class);
        }
        catch (NoSuchMethodException | SecurityException e)
        {
            throw new RuntimeException(e);
        }
    }
}
