package org.example.studycafe.common.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@WebMvcTest(
    controllers = [SecurityConfigTest.TestController::class],
    properties = ["app.cors.allowed-origins[0]=http://localhost:5173"],
)
@Import(SecurityConfig::class)
class SecurityConfigTest(
    @Autowired private val mockMvc: MockMvc,
) {

    @Test
    fun `permits all requests`() {
        mockMvc.perform(get("/test"))
            .andExpect(status().isOk)
    }

    @Test
    fun `allows configured origin in preflight requests`() {
        mockMvc.perform(
            options("/test")
                .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"),
        )
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173"))
    }

    @Test
    fun `rejects disallowed origin in preflight requests`() {
        mockMvc.perform(
            options("/test")
                .header(HttpHeaders.ORIGIN, "http://evil.com")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"),
        )
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
    }

    @RestController
    private class TestController {
        @GetMapping("/test")
        fun test(): String = "ok"
    }
}
