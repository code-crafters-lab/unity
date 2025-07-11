import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("ccl.lib")
    id("ccl.publish.aliyun")
    id("ccl.publish.nexus3")
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "org.codecrafterslab"
description = "Unity Gradle Plugin"

dependencies {

    implementation(platform("org.springframework:spring-framework-bom:5.3.39"))
    implementation(platform("org.junit:junit-bom:5.11.3"))

    implementation("org.springframework:spring-core")
    implementation("org.apache.maven:maven-artifact:3.9.6")
    implementation("de.skuzzle:semantic-version:2.1.1")

    /* gradle 插件 */
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("org.gradle:test-retry-gradle-plugin:1.5.6")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")

    /* 内置插件应用 */
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    plugins {
        create("bom") {
            id = "ccl.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
        }
        create("lib") {
            id = "ccl.lib"
            implementationClass = "org.codecrafterslab.build.LibraryPlugin"
        }
        create("app") {
            id = "ccl.app"
            implementationClass = "org.codecrafterslab.build.AppPlugin"
        }

        create("publish") {
            id = "ccl.publish"
            implementationClass = "org.codecrafterslab.publish.PublishPlugin"
        }
        create("publish-nexus") {
            id = "ccl.publish.nexus"
            implementationClass = "org.codecrafterslab.publish.NexusPlugin"
        }
        create("publish-aliyun") {
            id = "ccl.publish.aliyun"
            implementationClass = "org.codecrafterslab.publish.AliYunPlugin"
        }

        /* settings 插件 */
        create("repository") {
            id = "ccl.repo"
            implementationClass = "org.codecrafterslab.build.settings.RepositoriesPlugin"
        }
        create("settings") {
            id = "ccl.settings"
            implementationClass = "org.codecrafterslab.build.settings.ConventionsPlugin"
        }
//        create("AutoInclude") {
//            id = "io.github.coffee377.auto-include"
//            implementationClass = "org.codecrafterslab.build.settings.conventions.AutoIncludeConventions"
//            displayName = "Auto Include"
//            description = "auto include project for root project"
//        }
    }

}

tasks {
    withType(JavaCompile::class.java) {
        options.release.set(17)
        // options.compilerArgs.add("-Xlint:deprecation")
    }
    withType(KotlinCompile::class.java) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

    }

    withType(Jar::class.java) {
        /* 重复文件策略 */
        // duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
