plugins {
    id("com.voc.lib")
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    api(project(":dict-api"))

    optional("org.springframework.boot:spring-boot-starter-web")
    optional("org.springframework.boot:spring-boot-devtools")
    optional("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    optional("com.baomidou:mybatis-plus-boot-starter")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

