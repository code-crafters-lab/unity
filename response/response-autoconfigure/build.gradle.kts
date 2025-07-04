plugins {
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))
    optional(project(":response-core"))

    optional("org.springframework.boot:spring-boot-starter-web")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
