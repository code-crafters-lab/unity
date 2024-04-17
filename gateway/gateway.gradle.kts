plugins {
    `java-library`
    com.voc.anno
    id("com.google.protobuf")
    idea
}

dependencies {
    implementation(enforcedPlatform(project(":dependencies")))

    implementation("org.slf4j:slf4j-api")
    implementation("com.google.protobuf:protobuf-java")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("org.junit.jupiter:junit-jupiter")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.projectlombok" && requested.name == "lombok") {
//                println("${requested.group}:${requested.name}:${requested.version}")
                useVersion("1.18.32")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.26.1"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
        }
    }

    generateProtoTasks {
        all().configureEach {
            plugins {
                create("grpc") {
                }
            }
        }
    }

}