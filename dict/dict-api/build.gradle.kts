plugins {
    id("com.voc.lib")
    id("com.voc.grpc")
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))

    api("org.codecrafterslab.unity:exception-core")
    
    implementation("org.springframework:spring-core")

    runtimeOnly("org.slf4j:slf4j-api")

    compileOnly("ch.qos.logback:logback-classic")
    compileOnly("com.google.protobuf:protobuf-java")
    compileOnly("io.grpc:grpc-protobuf")
    compileOnly("io.grpc:grpc-stub")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter")
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
            }
        }
    }
}


