package org.codecrafterslab.unity.dict.boot.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.codecrafterslab.unity.dict.boot.MockMvcConfiguration;
import org.codecrafterslab.unity.dict.boot.provider.ProviderConfiguration;
import org.codecrafterslab.unity.dict.boot.app.entity.Goods;
import org.codecrafterslab.unity.dict.boot.app.entity.ProductService;
import org.codecrafterslab.unity.dict.boot.app.entity.Sex;
import org.codecrafterslab.unity.dict.boot.app.entity.User;
import org.codecrafterslab.unity.dict.boot.app.service.GoodService;
import org.codecrafterslab.unity.dict.boot.app.service.UserService;
import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.codecrafterslab.unity.dict.boot.json.DictJsonConfiguration;
import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Wu Yujie
 */
@Slf4j
@WebMvcTest(DictController.class)
@ContextConfiguration(classes = {
        MockMvcConfiguration.class,
        DictController.class,
        DictItemConverterConfiguration.class,
        ProviderConfiguration.class,
        DictJsonConfiguration.class})
class DictControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GoodService goodService;

    @Test
    @DisplayName("枚举 code 转换")
    void enumDictItem1() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("1").name("demo1").sex(sex).valueSex(sex).labelSex(sex).codeSex(sex).allSex(sex)
                        .build();
        given(this.userService.getSex(sex)).willReturn(user);

        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getCode()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sex']").value(Sex.MALE.getCode()))
                .andDo(print());

    }

    @Test
    @DisplayName("枚举 value 转换")
    void enumDictItem2() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("2").name("demo2").sex(sex).valueSex(sex).labelSex(sex).codeSex(sex).allSex(sex)
                        .build();
        given(this.userService.getSex(sex)).willReturn(user);
        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getValue()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sex']").value(Sex.MALE.getCode()));

    }

    @Test
    @DisplayName("枚举 value 转换")
    void enumDictItem3() throws Exception {
        Sex sex = Sex.MALE;
        User user =
                User.builder().id("3").name("demo3").sex(sex).valueSex(sex)
                        .labelSex(sex).codeSex(sex).allSex(sex).build();
        given(this.userService.getSex(sex)).willReturn(user);

        mvc.perform(get("/dict/converter/enum?sex={sex}", Sex.MALE.getValue().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sex']").value(Sex.MALE.getCode()));

    }

    @Test
    @DisplayName("枚举值不存在")
    void enumDictItem4() throws Exception {
        given(this.userService.getSex(null)).willThrow(new BizException(BizStatus.UN_SUPPORTED_VALUE));

        mvc.perform(get("/dict/converter/enum?sex={sex}", 4))
                .andExpect(status().is(BizStatus.UN_SUPPORTED_VALUE.getHttpStatus()));

    }

    @Test
    @DisplayName("功能字典枚举数组反序列化")
    void funcEnumDictItem1() throws Exception {
        Goods goods = Goods.builder()
                .services(Arrays.asList(ProductService.HOME_DELIVERY, ProductService.TRAINING))
                .services2(new ProductService[]{ProductService.INSTALLATION_AND_DEBUGGING,
                        ProductService.SECONDARY_DEVELOPMENT}).build();
        given(this.goodService.save(goods)).willReturn(goods);

        mvc.perform(post("/dict/func/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"services\": [\"送货上门\", \"培训\"],\"services2\": [2,8]}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.services").isArray())
                .andExpect(jsonPath("$.services.length()").value(2))
                .andExpect(jsonPath("$.services[*].name").value(containsInAnyOrder("培训", "送货上门")))
        ;
    }

    @Test
    @DisplayName("功能字典枚举大整数反序列化")
    void funcEnumDictItem2() throws Exception {
        List<ProductService> all = EnumDictItem.findAll(ProductService.class);
        Goods goods = Goods.builder().services(all).services2(all.toArray(new ProductService[0])).build();
        given(this.goodService.save(goods)).willReturn(goods);

        mvc.perform(post("/dict/func/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"services\": 15,\"services2\": 15}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding(StandardCharsets.UTF_8.displayName()))
                .andExpect(jsonPath("$.services").isArray())
                .andExpect(jsonPath("$.services.length()").value(4))
                .andExpect(jsonPath("$.services[*].value").value(containsInAnyOrder(8, 4, 2, 1)))
        ;

    }

}
