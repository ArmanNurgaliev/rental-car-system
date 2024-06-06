package ru.arman;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestDataConfiguration {

    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("my-test-db")
            .withUsername("postgres")
            .withPassword("password");

    static {
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    }
}
