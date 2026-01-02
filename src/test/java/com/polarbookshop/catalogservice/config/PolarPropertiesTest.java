package com.polarbookshop.catalogservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class PolarPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues("polar.greeting=Hello test catalog");

    @Test
    void bindsGreetingProperty() {

        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(PolarProperties.class);
            PolarProperties properties = context.getBean(PolarProperties.class);
            assertThat(properties.getGreeting()).isEqualTo("Hello test catalog");
        });
    }

    @Configuration
    @EnableConfigurationProperties(PolarProperties.class)
    static class TestConfig {
    }
}
