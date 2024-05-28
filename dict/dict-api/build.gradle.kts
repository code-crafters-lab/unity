plugins {
    id("com.voc.lib")
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    api("org.codecrafterslab.unity:exception-core")

    implementation("org.springframework:spring-core")

    optional("org.slf4j:slf4j-api")
    optional("ch.qos.logback:logback-classic")
//    compileOnly("com.google.protobuf:protobuf-java")
//    compileOnly("io.grpc:grpc-protobuf")
//    compileOnly("io.grpc:grpc-stub")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


