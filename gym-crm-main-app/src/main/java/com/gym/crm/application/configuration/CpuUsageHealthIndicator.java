package com.gym.crm.application.configuration;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class CpuUsageHealthIndicator implements HealthIndicator {

    private static final double THRESHOLD = 0.9;

    @Override
    public Health health() {
        OperatingSystemMXBean osBean =
                ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getCpuLoad();

        if (cpuLoad > THRESHOLD) {
            return Health.down()
                    .withDetail("cpuLoad", cpuLoad)
                    .withDetail("threshold", THRESHOLD)
                    .withDetail("status", "High CPU usage")
                    .build();
        }

        return Health.up()
                .withDetail("cpuLoad", cpuLoad)
                .build();
    }

}
