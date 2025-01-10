package com.service.working.hours.cucumber.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoTestConfig {

    @Value("${spring.data.mongodb.uri}")
    public String dbUrl;

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create(dbUrl), getDbName(dbUrl));
    }

    private String getDbName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}