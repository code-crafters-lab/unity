import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    id("com.voc.lib")
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    maven {
        url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
        isAllowInsecureProtocol = true
    }
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    mavenCentral()
}

dependencies {

    implementation(platform("org.springframework:spring-framework-bom:5.3.34"))

    implementation("org.springframework:spring-core")
    implementation("org.apache.maven:maven-artifact:3.9.6")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("de.skuzzle:semantic-version:2.1.1")

}

gradlePlugin {
    plugins {
    }

}

sourceSets {
    create("dependency-management") {
        java {
            srcDirs("src/dependency-management")
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


