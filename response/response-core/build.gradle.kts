plugins {
    id("ccl.lib")
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    api(project(":response-api"))

    implementation("org.codecrafterslab.unity:exception-core")

    optional("org.springframework.boot:spring-boot-starter-web")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
