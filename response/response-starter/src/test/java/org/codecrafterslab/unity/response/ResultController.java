package org.codecrafterslab.unity.response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/07/24 14:26
 */
@RestController
public class ResultController {

    @GetMapping("/global/void")
    public void result0() {
    }

    @GetMapping(value = "/global/string")
    public String result1() {
        return "返回字符串";
    }

    @GetMapping("/global/number")
    public Number result2() {
        return 1;
    }

    @GetMapping("/global/boolean")
    public boolean result3() {
        return true;
    }

}
