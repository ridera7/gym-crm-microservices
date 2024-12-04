package com.gym.crm.application.configuration;

import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.lang.management.ManagementFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CpuUsageHealthIndicatorTest {

    public static final Status UP_STATUS = Health.up().build().getStatus();
    public static final Status DOWN_STATUS = Health.down().build().getStatus();

    @Mock
    private OperatingSystemMXBean mockOsBean;

    @Mock
    private MockedStatic<ManagementFactory> mockedFactory;

    @InjectMocks
    private CpuUsageHealthIndicator sut;

    @ParameterizedTest
    @CsvSource({"0.0", "0.1", "0.2", "0.3", "0.5", "0.7", "0.9"})
    void shouldSetHealthStatusIsUpWhenCpuLoadBelowThreshold(double cpuLoad) {
        mockedFactory.when(() -> ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class)).thenReturn(mockOsBean);
        when(mockOsBean.getCpuLoad()).thenReturn(cpuLoad);

        Health actual = sut.health();
        
        assertEquals(UP_STATUS, actual.getStatus());
        assertTrue(actual.getDetails().containsKey("cpuLoad"));
        assertEquals(cpuLoad, actual.getDetails().get("cpuLoad"));
    }

    @ParameterizedTest
    @CsvSource({"0.91", "0.95", "0.99"})
    void shouldSetHealthStatusIsDownWhenCpuLoadAboveThreshold(double cpuLoad) {
        double cpuLoadThreshold = 0.9;
        String warningStatus = "High CPU usage";
        mockedFactory.when(() -> ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class)).thenReturn(mockOsBean);
        when(mockOsBean.getCpuLoad()).thenReturn(cpuLoad);

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
        assertTrue(actual.getDetails().containsKey("cpuLoad"));
        assertEquals(cpuLoad, actual.getDetails().get("cpuLoad"));
        assertEquals(cpuLoadThreshold, actual.getDetails().get("threshold"));
        assertEquals(warningStatus, actual.getDetails().get("status"));
    }

}