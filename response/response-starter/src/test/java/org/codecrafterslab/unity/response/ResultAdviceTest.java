package org.codecrafterslab.unity.response;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.response.autoconfigure.ResponseAutoConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 12:07
 */
@Slf4j
@WebMvcTest
@WebAppConfiguration
@ContextConfiguration(classes = {ResponseAutoConfiguration.class, ResultController.class})
@Disabled
class ResultAdviceTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("全局成功响应")
    void globalWrapperVoid() throws Exception {
        mockMvc.perform(get("/global/void"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['success']").value(true))
                .andExpect(jsonPath("$.['data']").isEmpty());
    }

    @Test
    @DisplayName("全局成功字符串")
    void globalWrapperString() throws Exception {
        mockMvc.perform(get("/global/string"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(mockMvcResult -> log.info(mockMvcResult.getResponse().getContentAsString()))
//                .andExpect(jsonPath("$.['data']").value("返回字符串"))
        ;
    }

    @Test
    @DisplayName("全局成功数字")
    void globalWrapperNumber() throws Exception {
        mockMvc.perform(get("/global/number"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['data']").value(1));
    }

    @Test
    @DisplayName("全局成功布尔类型")
    void globalWrapperBoolean() throws Exception {
        mockMvc.perform(get("/global/boolean"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['data']").value(true));
    }
}
