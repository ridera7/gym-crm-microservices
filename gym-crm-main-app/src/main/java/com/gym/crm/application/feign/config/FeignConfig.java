package com.gym.crm.application.feign.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor customHeaderInterceptor() {
        return requestTemplate -> {
            if (!requestTemplate.headers().containsKey("X-Request-X")) {
                requestTemplate.header("X-Request-X", "Gateway");
            }

            if (!requestTemplate.headers().containsKey("Authorization")) {
                requestTemplate.header("Authorization", "Bearer true");
            }
        };
    }
}
