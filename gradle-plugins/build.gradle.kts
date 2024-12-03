import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("ccl.lib")
    id("ccl.publish.aliyun")
    `kotlin-dsl`
//    kotlin("jvm") version "1.9.24"
//    id("org.jetbrains.kotlin.jvm") version "2.1.0"
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
        // todo deprecated remove in next
        create("bom-deprecated") {
            id = "com.voc.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
        }
        create("bom") {
            id = "ccl.bom"
            implementationClass = "org.codecrafterslab.build.bom.BomPlugin"
        }
        // todo deprecated remove in next
        create("lib-deprecated") {
            id = "com.voc.lib"
            implementationClass = "org.codecrafterslab.build.LibraryPlugin"
        }
        create("lib") {
            id = "ccl.lib"
            implementationClass = "org.codecrafterslab.build.LibraryPlugin"
        }
        // todo deprecated remove in next
        create("app-deprecated") {
            id = "com.voc.app"
            implementationClass = "org.codecrafterslab.build.AppPlugin"
        }
        create("app") {
            id = "ccl.app"
            implementationClass = "org.codecrafterslab.build.AppPlugin"
        }

        // todo deprecated remove in next
        create("publish-deprecated") {
            id = "com.voc.publish"
            implementationClass = "org.codecrafterslab.publish.PublishPlugin"
        }
        create("publish") {
            id = "ccl.publish"
            implementationClass = "org.codecrafterslab.publish.PublishPlugin"
        }
        // todo deprecated remove in next
        create("JinQiSoftNexus3") {
            id = "net.jqsoft.nexus3"
            implementationClass = "org.codecrafterslab.publish.Nexus3Plugin"
        }
        create("publish-nexus3") {
            id = "ccl.publish.nexus3"
            implementationClass = "org.codecrafterslab.publish.Nexus3Plugin"
        }
        create("publish-aliyun") {
            id = "ccl.publish.aliyun"
            implementationClass = "org.codecrafterslab.publish.AliYunPlugin"
        }

        /* settings 插件 */
        // todo deprecated remove in next
        create("repository-deprecated") {
            id = "com.voc.repo"
            implementationClass = "org.codecrafterslab.build.settings.RepositoriesPlugin"
        }
        create("repository") {
            id = "ccl.repo"
            implementationClass = "org.codecrafterslab.build.settings.RepositoriesPlugin"
        }

        // todo deprecated remove in next
        create("settings-deprecated") {
            id = "com.voc.settings"
            implementationClass = "org.codecrafterslab.build.settings.ConventionsPlugin"
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
        options.compilerArgs.add("-Xlint:deprecation")
    }
    withType(KotlinCompile::class.java) {

        compilerOptions {
            languageVersion.set(KotlinVersion.KOTLIN_1_9)
            apiVersion.set(KotlinVersion.KOTLIN_1_9)
            suppressWarnings.set(true)
            if (!freeCompilerArgs.get().contains("-Xsuppress-version-warnings")) {
//                freeCompilerArgs.add("-Xsuppress-version-warnings")
            }
            progressiveMode.set(true)
            verbose.set(true)
        }

    }

    withType(Jar::class.java) {
        /* 重复文件策略，排除 */
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

//publishing {
//    repositories {
//        maven {
//            name = "AliYun"
//            credentials {
//                username = "5f4ba059fa82bfeb805a1e09"
//                password = "a3XkZLNApybs"
//            }
//            url = uri("https://packages.aliyun.com/5f6a9b06d24814603933faab/maven/2038604-snapshot-xnrepo")
//        }
//    }
//}