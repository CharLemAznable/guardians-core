package com.github.charlemaznable.guardians.context;

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

import java.util.Map;

import static com.github.charlemaznable.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = com.github.charlemaznable.guardians.context.ContextConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class ContextTest {

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
    public void testAlpha() {
        val response = mockMvc.perform(get("/context/alpha"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        String responseContent = response.getContentAsString();
        Map<String, Object> responseMap = unJson(responseContent);
        assertEquals("GuardContextAlpha", responseMap.get("prefix"));
        assertEquals("GuardContextAlpha", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testBeta() {
        val response = mockMvc.perform(get("/context/beta"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        String responseContent = response.getContentAsString();
        Map<String, Object> responseMap = unJson(responseContent);
        assertEquals("GuardContextBeta", responseMap.get("prefix"));
        assertEquals("GuardContextBeta", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testError() {
        val response = mockMvc.perform(get("/context/error"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        String responseContent = response.getContentAsString();
        Map<String, Object> responseMap = unJson(responseContent);
        assertEquals("Error", responseMap.get("prefix"));
        assertEquals("Error", responseMap.get("suffix"));
    }
}
