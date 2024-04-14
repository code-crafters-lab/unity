import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `java-library`
    `kotlin-dsl`
    idea
}

//sourceCompatibility
//targetCompatibility = 17


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
            cacheDynamicVersionsFor(10, TimeUnit.MINUTES)
            cacheChangingModulesFor(10, TimeUnit.SECONDS)
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

    implementation(platform("org.springframework:spring-framework-bom:6.1.6"))
//    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-core")
//    implementation("org.springframework:spring-web")
//    implementation("org.apache.maven:maven-embedder:3.9.6")
    implementation("org.apache.maven:maven-artifact:3.9.6")

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
//        options.sor
//        options.release.set(8)
        options.encoding = "UTF-8"
//    options.compilerArgs.add("-Xlint:deprecation")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    register("lib", Copy::class.java) {
        group = "build"
        from(configurations.runtimeClasspath)
        into("${layout.buildDirectory.get().asFile.path}/lib")
    }
}

gradlePlugin {
    plugins {
        create("bom") {
            id = "com.voc.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
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