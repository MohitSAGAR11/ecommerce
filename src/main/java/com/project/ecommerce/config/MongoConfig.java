package com.project.ecommerce.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public CommandLineRunner testMongoConnection(MongoClient mongoClient) {
        return args -> {
            try {
                mongoClient.listDatabaseNames().first();
                System.out.println("✅ Successfully connected to MongoDB Atlas!");
            } catch (Exception e) {
                System.err.println("❌ Failed to connect to MongoDB Atlas: " + e.getMessage());
            }
        };
    }
}