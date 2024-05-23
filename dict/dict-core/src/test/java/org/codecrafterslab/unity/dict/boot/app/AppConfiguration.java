package org.codecrafterslab.unity.dict.boot.app;

import org.codecrafterslab.unity.dict.boot.app.service.impl.GoodServiceImpl;
import org.codecrafterslab.unity.dict.boot.app.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({UserServiceImpl.class, GoodServiceImpl.class})
public class AppConfiguration {
}
