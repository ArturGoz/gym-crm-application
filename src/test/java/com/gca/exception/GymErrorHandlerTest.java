package com.gca.exception;

import com.gca.config.WebConfig;
import com.gca.controller.TestExceptionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = {WebConfig.class, TestExceptionController.class, GymErrorHandler.class})
@WebAppConfiguration
public class GymErrorHandlerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        mockMvc.perform(get("/test/service"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value("This is a test ServiceException"));
    }

    @Test
    void shouldHandleDaoException() throws Exception {
        mockMvc.perform(get("/test/dao"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value("Data Access Error"));
    }

    @Test
    void shouldHandleUnhandledException() throws Exception {
        mockMvc.perform(get("/test/unhandled"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value("Something went wrong."));
    }
}