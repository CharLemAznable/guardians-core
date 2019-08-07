package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.joor.ReflectException;
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

import static com.github.charlemaznable.codec.Json.unJson;
import static org.joor.Reflect.onClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ThreadLocalConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class ThreadLocalTest {

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
    public void testAlpha() {
        val response = mockMvc.perform(get("/threadlocal/alpha"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("PreAlphaGuardian", responseMap.get("prefix"));
        assertEquals("PreAlphaGuardian", responseMap.get("context"));
        assertEquals("alpha", responseMap.get("method"));
        assertEquals("ThreadLocalController", responseMap.get("class"));
    }

    @SneakyThrows
    @Test
    public void testBeta() {
        val response = mockMvc.perform(get("/threadlocal/beta"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertNull(responseMap.get("prefix"));
        assertEquals("PreBetaGuardian", responseMap.get("context"));
        assertEquals("beta", responseMap.get("method"));
        assertEquals("ThreadLocalController", responseMap.get("class"));
    }

    @Test
    public void testCoverage() {
        assertThrows(ReflectException.class,
                () -> onClass(GuardianContext.class).create().get());
    }
}
