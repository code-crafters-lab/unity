import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.voc.lib")
    id("com.voc.publish")
    id("net.jqsoft.nexus3")
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "org.codecrafterslab"
description = "Unity Gradle Plugin"

sourceSets {
    create("dependency-management") {
        java {
            srcDirs("src/dependency-management")
        }
    }
}

dependencies {
    implementation(platform("org.springframework:spring-framework-bom:5.3.34"))
    implementation(platform("org.junit:junit-bom:5.10.2"))

    implementation("org.springframework:spring-core")
    implementation("org.apache.maven:maven-artifact:3.9.6")
    implementation("de.skuzzle:semantic-version:2.1.1")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    implementation("org.gradle:test-retry-gradle-plugin:1.5.6")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    plugins {
        create("base") {
            id = "com.voc.base"
            implementationClass = "org.codecrafterslab.gradle.BasePlugin"
        }
        create("grpc") {
            id = "com.voc.grpc"
            implementationClass = "org.codecrafterslab.gradle.plugins.GRPCPlugin"
        }
        create("bom") {
            id = "com.voc.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
        }
        create("lib") {
            id = "com.voc.lib"
            implementationClass = "org.codecrafterslab.build.LibraryPlugin"
        }
        create("app") {
            id = "com.voc.boot"
            implementationClass = "org.codecrafterslab.build.AppPlugin"
        }
        create("publish") {
            id = "com.voc.publish"
            implementationClass = "org.codecrafterslab.build.PublishPlugin"
        }
        create("repository") {
            id = "com.voc.repo"
            implementationClass = "org.codecrafterslab.build.settings.conventions.RepositoriesConventions"
        }
        create("JinQiSoftNexus3") {
            id = "net.jqsoft.nexus3"
            implementationClass = "net.jqsoft.Nexus3Plugin"
        }
    }

}

tasks {
    withType(JavaCompile::class.java) {
        options.release.set(17)
    }
    withType(KotlinCompile::class.java) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}