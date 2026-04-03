package org.example.studycafe.common.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration
import org.springframework.context.annotation.Configuration

class CorsPropertiesTest {

    private val contextRunner = ApplicationContextRunner()
        .withConfiguration(
            AutoConfigurations.of(
                ConfigurationPropertiesAutoConfiguration::class.java,
                ValidationAutoConfiguration::class.java,
            ),
        )
        .withUserConfiguration(CorsPropertiesTestConfiguration::class.java)

    @Test
    fun `fails fast when allowed origins are missing`() {
        contextRunner.run { context ->
            assertThat(context).hasFailed()
            assertThat(context.startupFailure)
                .hasStackTraceContaining("app.cors.allowed-origins must not be empty")
        }
    }

    @Test
    fun `binds when allowed origins are provided`() {
        contextRunner
            .withPropertyValues("app.cors.allowed-origins[0]=http://localhost:5173")
            .run { context ->
                assertThat(context).hasNotFailed()
                assertThat(context.getBean(CorsProperties::class.java).allowedOrigins)
                    .containsExactly("http://localhost:5173")
            }
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(CorsProperties::class)
    private class CorsPropertiesTestConfiguration
}
