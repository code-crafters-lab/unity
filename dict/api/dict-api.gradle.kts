plugins {
    `java-library`
    com.voc.anno
    com.voc.grpc
//    id("io.spring.dependency-management") version "1.1.4"
}

//dependencyManagement {
//    imports {
//        mavenBom("org.springframework.boot:spring-boot-dependencies:2.5.15")
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.6")
//        mavenBom("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2021.1")
//        mavenBom("com.google.protobuf:protobuf-bom:4.26.1")
//        mavenBom("io.grpc:grpc-bom:1.63.0")
//    }
//
//    dependencies {
//        dependency("org.mybatis.spring:mybatis-spring-boot-starter:2.3.2") {
////                exclude("org.springframework.boot:spring-boot-starter")
////                exclude("org.springframework.boot:spring-boot-starter-jdbc")
//        }
//        dependency("com.baomidou:mybatis-plus-boot-starter:3.5.5")
//    }
//    println("lombok 导入版本号 ：${dependencyManagement.importedProperties["lombok.version"]}")
//    println("lombok 项目覆盖版本号 ：${ext.properties["lombok.version"]}")
//    println("lombok BOM配置版本号 ：${ext.properties["lombok.version"]}")
//    generatedPomCustomization {
////        enabled(false)
//    }
//}

dependencies {
    implementation(platform(project(":dependencies")))

    implementation("org.slf4j:slf4j-api")
    implementation("com.google.protobuf:protobuf-java")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    implementation(project(":exception:exception-api"))
    implementation(project(":exception:exception-core"))

    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    compileOnly("com.baomidou:mybatis-plus-boot-starter")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
                if ("${it.group}:${it.name}" == "org.springframework.boot:spring-boot-configuration-processor") {
                    it.version { require("2.5.15") }
                }
                println("=> ${it.group}:${it.name}:${it.version}")
            }
        }
    }
}

//testing {
//    suites {
//        getting(JvmTestSuite::class) {
//            useJUnitJupiter()
//        }
//    }
//}
//tasks.test {
//    useJUnitPlatform()
//}



