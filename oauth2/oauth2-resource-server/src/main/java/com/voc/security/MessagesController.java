package com.voc.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/03/28 20:04
 */
@RestController
public class MessagesController {

    @GetMapping("/messages")
    public String[] getMessages() {
        return new String[]{"Message 1", "Message 2", "Message 3"};
    }

    @GetMapping("/demo")
    public String[] getDemo() {
        return new String[]{"Demo 1", "Demo 2", "Demo 3"};
    }

    @GetMapping("/menus")
    public String[] getDemo2() {
        return new String[]{"menu 1", "menu 2", "menu 3"};
    }
}
