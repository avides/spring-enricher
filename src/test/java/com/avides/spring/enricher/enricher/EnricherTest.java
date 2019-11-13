package com.avides.spring.enricher.enricher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class EnricherTest
{
    @MockStrict
    private AnyService anyService;

    @Test
    public void testSuppressEnriching()
    {
        expect(anyService.getData()).andReturn("anyString");

        replayAll();
        assertThat(Enricher.suppressEnriching(() -> anyService.getData())).isEqualTo("anyString");
        verifyAll();
    }

    private interface AnyService
    {
        String getData();
    }
}
