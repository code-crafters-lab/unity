plugins {
    id("ccl.lib")
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
}
