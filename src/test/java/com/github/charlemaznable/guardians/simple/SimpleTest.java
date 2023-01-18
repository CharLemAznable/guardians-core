package com.github.charlemaznable.guardians.simple;

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

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(SimpleConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SimpleTest {

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
    public void testSimple() {
        val response = mockMvc.perform(get("/simple/simple").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());

        val response2 = mockMvc.perform(get("/simple/simple").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertTrue(responseMap2.isEmpty());
    }

    @SneakyThrows
    @Test
    public void testEmpty() {
        val response = mockMvc.perform(get("/simple/empty").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());
    }

    @SneakyThrows
    @Test
    public void testGuarding() {
        val response = mockMvc.perform(get("/simple/guarding").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("SimpleGuardian", responseMap.get("prefix"));
        assertEquals("SimpleGuardian", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testGuardingError() {
        val response = mockMvc.perform(get("/simple/guardingError").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());
    }

    @SneakyThrows
    @Test
    public void testGuardingFalse() {
        val response = mockMvc.perform(get("/simple/guardingFalse").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("FalseGuardian", responseMap.get("prefix"));
    }
}
