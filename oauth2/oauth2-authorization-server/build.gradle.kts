plugins {
    id("com.voc.app")
}

dependencies {


    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

//  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    /* 认证服务器 */
//    implementation(project(":oauth2-security"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    /* 页面 */
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

//    annotationProcessor("org.projectlombok:lombok")
//    implementation("org.projectlombok:lombok")
//    implementation("org.thymeleaf:thymeleaf-spring5")

//    implementation("org.mapstruct:mapstruct")

}
