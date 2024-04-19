package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Wu Yujie
 */
@Slf4j
@RestController
@RequestMapping(value = "/dict", name = "字典")
public class DictController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/converter/raw/value/integer", name = "原始方式 1")
    public User rawParameter1(Integer sex) {
        return userService.getSexByIntegerValue(sex);
    }

    @GetMapping(value = "/converter/raw/value/string", name = "原始方式 2")
    public User rawParameter2(String sex) {
        return userService.getSexByStringValue(sex);
    }

    @GetMapping(value = "/converter/raw/code", name = "原始方式 3")
    public User rawParameter3(String sex) {
        return userService.getSexByCode(sex);
    }

    @GetMapping(value = "/converter/enum", name = "枚举字典使用")
    public User enumParameter4(Sex sex) {
        return userService.getSex(sex);
    }
}
