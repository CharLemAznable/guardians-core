package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
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
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MultipleConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class MultipleTest {

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
        val response = mockMvc.perform(get("/multiple/default").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("MultipleAlphaGuardianMultipleBetaGuardian", responseMap.get("prefix"));
        assertEquals("MultipleBetaGuardianMultipleAlphaGuardian", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testCompose() {
        val response = mockMvc.perform(get("/multiple/compose").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("MultipleAlphaGuardianMultipleBetaGuardian", responseMap.get("prefix"));
        assertEquals("MultipleBetaGuardianMultipleAlphaGuardian", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testPlain() {
        val response = mockMvc.perform(get("/multiple/plain").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("MultipleAlphaGuardianMultipleBetaGuardian", responseMap.get("prefix"));
        assertEquals("MultipleBetaGuardianMultipleAlphaGuardian", responseMap.get("suffix"));
    }

    @SneakyThrows
    @Test
    public void testNone() {
        val response = mockMvc.perform(get("/multiple/none").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());

        val response2 = mockMvc.perform(get("/none/none").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertTrue(responseMap.isEmpty());
    }
}
