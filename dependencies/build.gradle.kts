plugins {
    id("com.voc.bom")
}

bom {
    effectiveBomArtifact()
//    library("ActiveMQ", "6.1.1") {
//        group("org.apache.activemq") {
//            setModules(listOf("activemq-amqp", "activemq-blueprint"))
//        }
//        links {
//            site("https://activemq.apache.org")
//            docs("https://activemq.apache.org/components/classic/documentation")
//            releaseNotes { version ->
//                "https://activemq.apache.org/components/classic/download/classic-%02d-%02d-%02d"
//                    .format(version.componentInts())
//            }
//        }
//    }
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
}
