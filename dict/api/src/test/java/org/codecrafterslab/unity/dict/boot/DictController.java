package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wu Yujie
 */
@Slf4j
@RestController
@RequestMapping(value = "/dict", name = "字典")
public class DictController {

    @GetMapping(value = "/converter/raw", name = "原始方式接受")
    public Sex converter(String sex) {
        Sex res = EnumDictItem.find(Sex.class, sex);
        log.info("输入 => {}  ,输出 => {}", sex, res);
        return res;
    }

    @GetMapping(value = "/converter/enum", name = "原始方式接受")
    public Sex converter(Sex sex) {
        log.info("输入 => {}  ,输出 => {}", sex, sex);
        return sex;
    }
}
