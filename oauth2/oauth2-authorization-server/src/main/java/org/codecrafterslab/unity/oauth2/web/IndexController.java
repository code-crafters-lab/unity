package org.codecrafterslab.unity.oauth2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Steve Riesenberg
 * @since 1.1
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String login() {
        return "index";
    }

}
