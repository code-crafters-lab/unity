plugins {
    com.voc.lib
}

dependencies {
    implementation(platform(project(":dependencies")))

    implementation(project(":exception:exception-api"))
    implementation(project(":exception:exception-core"))
    implementation(project(":dict:dict-api"))

    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    compileOnly("com.baomidou:mybatis-plus-boot-starter")

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

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.springframework.boot") {
                useVersion("2.5.15")
            }
        }
    }
}

configurations.annotationProcessor {
    withDependencies {
        dependencies.forEach {
            if (it.version == null && it is ExternalDependency) {
                if ("${it.group}:${it.name}" == "org.projectlombok:lombok") {
                    it.version { require("1.18.26") }
                }
                if ("${it.group}:${it.name}" == "org.springframework.boot:spring-boot-configuration-processor") {
                    it.version { require("2.5.15") }
                }
                println("=> ${it.group}:${it.name}:${it.version}")
            }
        }
    }
}


