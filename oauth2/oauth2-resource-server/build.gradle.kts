plugins {
    id("com.voc.lib")
}

dependencies {
//  implementation(project(":$common:$common-api"))
//  implementation(project(":${parent?.name}:${parent?.name}-security"))
//    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-web") {
//        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }

    /* 资源服务器 */
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//    implementation("com.nimbusds:oauth2-oidc-sdk")


    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-cache")

}
