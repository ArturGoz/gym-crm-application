package com.gca.config;

import com.gca.security.jwt.JwtTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfig.class)
class SecurityConfigTest {

    @MockitoBean
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void corsProperties(DynamicPropertyRegistry registry) {
        registry.add("cors.allowed-origins", () -> "https://example.com");
    }

    @Test
    void whenAllowedOriginIsSpecific_thenSpecificOriginIsAllowed() throws Exception {
        mockMvc.perform(options("/gym-crm-core/api/v1/trainees/register")
                        .header(HttpHeaders.ORIGIN, "https://example.com")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.POST.name()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://example.com"));
    }

    @Test
    void whenAllowedOriginIsSpecific_thenOtherOriginsAreDenied() throws Exception {
        mockMvc.perform(options("/gym-crm-core/api/v1/trainees/register")
                        .header(HttpHeaders.ORIGIN, "https://notallowed.com")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.POST.name()))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
