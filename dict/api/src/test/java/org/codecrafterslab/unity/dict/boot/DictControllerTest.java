package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.config.MockMvcConfiguration;
import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Wu Yujie
 */
@Slf4j
@WebMvcTest(DictController.class)
@Import({MockMvcConfiguration.class, DictItemConverterConfiguration.class})
class DictControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("枚举 code 转换")
    void enumDictItem1() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("1").name("demo1").sex(sex).valueSex(sex).labelSex(sex).codeSex(sex).allSex(sex).build();
        given(this.userService.getSex(sex)).willReturn(user);

        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getCode()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sex']").value(Sex.MALE.name()));

    }

    @Test
    @DisplayName("枚举 value 转换")
    void enumDictItem2() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("2").name("demo2").sex(sex).valueSex(sex).labelSex(sex).codeSex(sex).allSex(sex).build();
        given(this.userService.getSex(sex)).willReturn(user);
        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getValue())).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.['sex']").value(Sex.MALE.name()));

    }

    @Test
    @DisplayName("枚举 value 转换")
    void enumDictItem3() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("3").name("demo3").sex(sex).valueSex(sex).labelSex(sex).codeSex(sex).allSex(sex).build();
        given(this.userService.getSex(sex)).willReturn(user);

        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getValue().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sex']").value(Sex.MALE.name()));

    }

    @Test
    @DisplayName("枚举值不存在")
    void enumDictItem4() throws Exception {
        given(this.userService.getSex(null)).willThrow(new BizException(BizStatus.UN_SUPPORTED_VALUE));

        mvc.perform(get("/dict/converter/enum?sex={sex}", 4)).
                andExpect(status().is(BizStatus.UN_SUPPORTED_VALUE.getHttpStatus()))
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        ;

    }

}
