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
    library("Spring Cloud", "2020.0.6") {
        group("org.springframework.cloud") {
            imports = listOf("spring-cloud-dependencies")
        }
    }
    library("Spring Cloud Alibaba", "2021.1") {
        group("com.alibaba.cloud") {
            imports = listOf("spring-cloud-alibaba-dependencies")
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
    library("Mybatis Boot", "2.3.2") {
        group("org.mybatis.spring.boot") {
            module(
                "mybatis-spring-boot-starter",
                "org.springframework.boot:spring-boot-starter", "org.springframework.boot:spring-boot-starter-jdbc"
            )
        }
    }
    library("Mybatis Plus Boot", "3.5.5") {
        group("com.baomidou") {
            setModules(listOf("mybatis-plus-boot-starter"))
        }
    }
}
