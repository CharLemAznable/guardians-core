package com.github.charlemaznable.guardians.annotation;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(AnnotationConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AnnotationTest {

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
    public void testDefault() {
        val response = mockMvc.perform(get("/annotation/default").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("DEFAULT", responseMap.get("prefix"));
        assertEquals("DEFAULT", responseMap.get("first"));
        assertEquals("DEFAULT", responseMap.get("list"));
        assertEquals("DEFAULT", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testAlpha() {
        val response = mockMvc.perform(get("/annotation/alpha").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("ALPHA", responseMap.get("prefix"));
        assertEquals("z", responseMap.get("first"));
        assertEquals("z", responseMap.get("list"));
        assertEquals("ALPHA", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testBeta() {
        val response = mockMvc.perform(get("/annotation/beta").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("BETA", responseMap.get("prefix"));
        assertEquals("z", responseMap.get("first"));
        assertEquals("zyx", responseMap.get("list"));
        assertEquals("BETA", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testGamma() {
        val response = mockMvc.perform(get("/annotation/gamma").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("GAMMA", responseMap.get("prefix"));
        assertEquals("z", responseMap.get("first"));
        assertEquals("z", responseMap.get("list"));
        assertEquals("GAMMA", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testDelta() {
        val response = mockMvc.perform(get("/annotation/delta").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("DELTA", responseMap.get("prefix"));
        assertEquals("z", responseMap.get("first"));
        assertEquals("zyx", responseMap.get("list"));
        assertEquals("DELTA", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testError() {
        val response = mockMvc.perform(get("/annotation/error").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Empty", responseMap.get("prefix"));
        assertEquals("Empty", responseMap.get("first"));
        assertEquals("Empty", responseMap.get("list"));
        assertEquals("Empty", responseMap.get("suffix"));
    }
}
