package com.service.working.hours.configuration;

import com.service.working.hours.entity.Trainer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void ensureIndexes() {
//        IndexOperations trainerIndexOps = mongoTemplate.indexOps(Trainer.class);
//        trainerIndexOps.ensureIndex(new Index().on("firstName", org.springframework.data.domain.Sort.Direction.ASC));
//        trainerIndexOps.ensureIndex(new Index().on("lastName", org.springframework.data.domain.Sort.Direction.ASC));
    }
}

