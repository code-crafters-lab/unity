plugins {
    id("com.voc.bom")
}

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
