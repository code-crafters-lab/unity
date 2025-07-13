plugins {
}

dependencies {
    api(project(":response-api"))
    api(project(":response-core"))
    api(project(":response-autoconfigure"))

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.codecrafterslab.unity:exception-core")
}
