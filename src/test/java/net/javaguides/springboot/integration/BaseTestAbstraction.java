package net.javaguides.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class BaseTestAbstraction {
    static final MySQLContainer MY_SQL_CONTAINER;
    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest").withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems")
        ;
        // Note: this class cannot use the @Testcontainers extension, so we have to manage (i.e. start) the container
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        // NOTE: this also works, but the value provided is resolved immediately instead of being a callback method
//        System.setProperty("spring.datasource.url", mySqlContainer.getJdbcUrl());
    }
}
