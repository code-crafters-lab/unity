package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.boot.converter.DictItemConverterConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author Wu Yujie
 */
//@WebMvcTest(DictController.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {
//        DictJsonConfiguration.class, MockMvcConfiguration.class,
//})
@Slf4j
@SpringJUnitWebConfig({DictItemConverterConfiguration.class, MockMvcConfiguration.class})
//@SpringBootTest
//@AutoConfigureMockMvc
class DictControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DictController()).build();
    }

//    @Test
//    void enumDictItem() throws Exception {
//        mockMvc.perform(get("/dict/enum"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
////                .andExpect(jsonPath("$.['success']").value(true))
////                .andExpect(jsonPath("$.['data']").isEmpty())
//        ;
//    }

//    @Test
//    void dataDictItem() throws Exception {
//        mockMvc.perform(get("/dict/data"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print());
////                .andExpect(jsonPath("$.['success']").value(true))
////                .andExpect(jsonPath("$.['data']").isEmpty());
//    }

}
