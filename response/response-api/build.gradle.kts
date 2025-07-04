plugins {
    id("ccl.lib")
}


dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    compileOnly(project(":response-core"))

    implementation("org.springframework:spring-core")

}
