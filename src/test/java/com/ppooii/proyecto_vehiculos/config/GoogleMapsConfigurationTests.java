package com.ppooii.proyecto_vehiculos.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.StandardEnvironment;

import com.ppooii.proyecto_vehiculos.APIs.GoogleMaps.Geocoder;

class GoogleMapsConfigurationTests {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withInitializer(context -> context.getEnvironment().getPropertySources()
                    .remove(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME))
            .withConfiguration(AutoConfigurations.of(PropertyPlaceholderAutoConfiguration.class))
            .withUserConfiguration(Geocoder.class);

    @Test
    void requiresAnExplicitNonemptyKey() {
        runner.run(context -> assertThat(context).hasFailed());
        runner.withPropertyValues("google.maps.api-key=")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void acceptsAConfiguredKeyWithoutCallingGoogle() {
        runner.withPropertyValues("google.maps.api-key=test-placeholder-not-used")
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).hasSingleBean(Geocoder.class);
                });
    }
}
