import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `java-library`
    `kotlin-dsl`
    idea
}

configurations {

    val annotationProcessor = configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
    val testAnnotationProcessor = configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
    val compileOnly = configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
    val runtimeOnly = configurations.getByName(JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME)

    testAnnotationProcessor {
        extendsFrom(annotationProcessor)
    }

    compileOnly {
        extendsFrom(annotationProcessor)
    }

    implementation {
        extendsFrom(annotationProcessor)
    }

    testCompileOnly {
        extendsFrom(compileOnly, testAnnotationProcessor)
    }

    runtimeClasspath {
        extendsFrom(annotationProcessor)
    }

    testRuntimeOnly {
        extendsFrom(runtimeOnly)
    }

    all {
        resolutionStrategy {
//            cacheDynamicVersionsFor(10, TimeUnit.MINUTES)
//            cacheChangingModulesFor(10, TimeUnit.SECONDS)
        }
    }

}

repositories {
    mavenLocal()
    maven {
        url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
        isAllowInsecureProtocol = true
    }
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
    mavenCentral()
}

dependencies {
//    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation(platform("org.springframework:spring-framework-bom:5.3.34"))
    implementation("org.springframework:spring-core")
    implementation("org.apache.maven:maven-artifact:3.9.6")

    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")


//  implementation(libs.spring.boot)
//  implementation(libs.dependency.management)
//  implementation(libs.semantic.versioning)
//
//  annotationProcessor(libs.lombok)
//
//  testImplementation(libs.junit.jupiter.api)
//  testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<Jar> {
        archiveBaseName.set("unity-gradle-plugin")
        /* 重复文件策略，排除 */
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
//        kotlinOptions.jvmTarget = "1.8"
    }

    register("lib", Copy::class.java) {
        group = "build"
        from(configurations.runtimeClasspath)
        into("${layout.buildDirectory.get().asFile.path}/lib")
    }
}

gradlePlugin {
    plugins {
        create("deployed") {
            id = "com.voc.deployed"
            implementationClass = "org.codecrafterslab.build.DeployedPlugin"
        }
        create("optional") {
            id = "com.voc.deployed"
            implementationClass = "org.codecrafterslab.build.optional.OptionalDependenciesPlugin"
        }
        create("anno") {
            id = "com.voc.anno"
            implementationClass = "org.codecrafterslab.build.AnnotationProcessorPlugin"
        }
        create("bom") {
            id = "com.voc.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
        }
        create("grpc") {
            id = "com.voc.grpc"
            implementationClass = "org.codecrafterslab.build.GRPCPlugin"
        }
//        create("lib") {
//            id = "com.voc.app.info"
//            implementationClass = "com.voc.gradle.plugin.AppInfoPlugin"
//        }
//        create("app") {
//            id = "com.voc.boot"
//            implementationClass = "com.voc.gradle.plugin.BootPlugin"
//        }
    }

}
