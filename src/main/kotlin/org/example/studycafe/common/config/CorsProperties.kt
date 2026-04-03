package org.example.studycafe.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import jakarta.validation.constraints.AssertTrue

@Validated
@ConfigurationProperties(prefix = "app.cors")
data class CorsProperties(
    val allowedOrigins: List<String> = emptyList(),
    val allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"),
    val allowedHeaders: List<String> = listOf(
        "Authorization",
        "Content-Type",
        "Accept",
        "X-Requested-With",
    ),
    val exposedHeaders: List<String> = listOf(
        "Authorization",
        "Content-Disposition",
        "Set-Cookie",
    ),
    val allowCredentials: Boolean = true,
    val maxAge: Long = 3600L,
) {

    @Suppress("unused") // invoked by Bean Validation framework via @AssertTrue
    @AssertTrue(message = "app.cors.allowed-origins must not be empty")
    fun hasAllowedOrigins(): Boolean = allowedOrigins.isNotEmpty()
}
