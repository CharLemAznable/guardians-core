package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExceptionConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class ExceptionTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();
    }

    @SneakyThrows
    @Test
    public void testTrue() {
        val response = mockMvc.perform(get("/exception/true"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("SUCCESSNo exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testFalse() {
        val response = mockMvc.perform(get("/exception/false"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("No exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testException() {
        val response = mockMvc.perform(get("/exception/exception"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionPostGuardian: ExceptionGuardian", responseContent);
    }

    @SneakyThrows
    @Test
    public void testExceptionUnhandled() {
        val response = mockMvc.perform(get("/exception/exceptionUnhandled"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("No exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testRuntime() {
        val response = mockMvc.perform(get("/exception/runtime"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionHandler: ExceptionRuntimeGuardian", responseContent);
    }

    @SneakyThrows
    @Test
    public void testRuntimeBiz() {
        val response = mockMvc.perform(get("/exception/runtimeBiz"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionHandler: BusinessRuntimeExceptionNo exception", responseContent);
    }
}
