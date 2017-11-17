package com.avides.spring.enricher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.avides.spring.enricher.annotation.Enriched;
import com.avides.spring.enricher.enricher.Enricher;
import com.avides.spring.enricher.testsupport.EnrichingTestSupport;

@RunWith(PowerMockRunner.class)
public class EnrichedExecutorTest extends EnrichingTestSupport
{
    @TestSubject
    private EnrichedExecutor enrichedExecutor = new EnrichedExecutor();

    @Mock
    private EnricherService enricherService;

    @Mock
    private ProceedingJoinPoint pjp;

    @Before
    public void setUp()
    {
        Function<ProceedingJoinPoint, Enriched> enrichedAnnotationSupplier = (joinPoint) ->
        {
            try
            {
                return getClass().getMethod("anyAnnotatedMethod").getAnnotation(Enriched.class);
            }
            catch (NoSuchMethodException | SecurityException e)
            {
                throw new RuntimeException(e);
            }
        };
        setInternalState(enrichedExecutor, enrichedAnnotationSupplier);
    }

    @Test
    public void testEnrich() throws Throwable
    {
        AnyObject anyObject = new AnyObject(null);

        expect(pjp.proceed()).andReturn(anyObject);

        enricherService.enrich(anyString(), anyObject(Class[].class), anyObject(Class[].class));
        expectLastCall().andAnswer(() ->
        {
            assertThat(getCurrentArguments()[0]).isSameAs(anyObject);
            assertThat(((Class[]) getCurrentArguments()[1])).containsOnly(AnyEnricher.class);
            assertThat(((Class[]) getCurrentArguments()[2])).containsOnly(AnyEnricher2.class);
            return null;
        });

        replayAll();
        assertThat(enrichedExecutor.enrich(pjp)).isSameAs(anyObject);
        verifyAll();
    }

    @Test
    public void testEnrichEnrichingIsSuppressed() throws Throwable
    {
        expect(pjp.proceed()).andReturn("anyValue");

        replayAll();
        assertThat(Enricher.suppressEnriching(() ->
        {
            try
            {
                return enrichedExecutor.enrich(pjp);
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        })).isEqualTo("anyValue");
        verifyAll();
    }

    @Enriched(value = AnyEnricher.class, ignore = AnyEnricher2.class)
    public void anyAnnotatedMethod()
    {
    }
}