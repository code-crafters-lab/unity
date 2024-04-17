plugins {
    id("com.voc.bom")
//    id("io.spring.dependency-management") version "1.1.4"
}

//dependencyManagement {
//    dependencies {
//        imports {
//            mavenBom("org.springframework.boot:spring-boot-dependencies:2.5.15")
//            mavenBom("com.google.protobuf:protobuf-bom:4.26.1")
//            mavenBom("io.grpc:grpc-bom:1.63.0")
//        }
//    }
//    println("lombok 导入版本号 ：${dependencyManagement.importedProperties["lombok.version"]}")
//    println("lombok 项目覆盖版本号 ：${ext.properties["lombok.version"]}")
//    println("lombok BOM配置版本号 ：${ext.properties["lombok.version"]}")
//    generatedPomCustomization {
////        enabled(false)
//    }
//}

bom {
    effectiveBomArtifact()
    library("Spring Boot", "2.5.15") {
        group("org.springframework.boot") {
            imports = listOf("spring-boot-dependencies")
        }
    }
    library("Envoy Proxy", "1.0.44") {
        group("io.envoyproxy.controlplane") {
            setModules(listOf("api", "cache", "server"))
        }
    }
    library("protobuf", "4.26.1") {
        group("com.google.protobuf") {
            imports = listOf("protobuf-bom")
        }
    }
    library("grpc", "1.63.0") {
        group("io.grpc") {
            imports = listOf("grpc-bom")
        }
    }
}
