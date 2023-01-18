package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(ExceptionConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ExceptionTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();
    }

    @SneakyThrows
    @Test
    public void testTrue() {
        val response = mockMvc.perform(get("/exception/true").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("SUCCESSNo exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testFalse() {
        val response = mockMvc.perform(get("/exception/false").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("No exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testException() {
        val response = mockMvc.perform(get("/exception/exception").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionPostGuardian: ExceptionGuardian", responseContent);
    }

    @SneakyThrows
    @Test
    public void testExceptionUnhandled() {
        val response = mockMvc.perform(get("/exception/exceptionUnhandled").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("No exception", responseContent);
    }

    @SneakyThrows
    @Test
    public void testRuntime() {
        val response = mockMvc.perform(get("/exception/runtime").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionHandler: ExceptionRuntimeGuardian", responseContent);
    }

    @SneakyThrows
    @Test
    public void testRuntimeBiz() {
        val response = mockMvc.perform(get("/exception/runtimeBiz").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        assertEquals("ExceptionHandler: BusinessRuntimeExceptionNo exception", responseContent);
    }
}
