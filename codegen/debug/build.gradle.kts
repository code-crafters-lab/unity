import org.openapitools.generator.gradle.plugin.tasks.GeneratorsTask
// 配置自定义 Generator 依赖（从本地仓库获取）
buildscript {

    dependencies {
        // 引入自定义 Generator JAR
        classpath(files(layout.buildDirectory.file("libs/openapi-0.1.0.jar")))
//        classpath 'com.example:custom-openapi-generator:1.0.0' // 替换为你的坐标
    }
}
plugins {
    id("org.openapi.generator") version "7.14.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}



dependencies {
    api(platform(libs.junit.bom))

    implementation(libs.openapi.generator)

//    implementation(project(":openapi"))

//    annotationProcessor(libs.google.auto.service)
//    annotationProcessor(libs.lombok)

//    runtimeOnly(libs.openapi.generator.cli)
//    runtimeOnly(files(layout.buildDirectory.file("libs/openapi-0.1.0.jar")))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

openApiGenerate {
    verbose.set(false)
    generatorName.set("java")
//    remoteInputSpec.set("http://127.0.0.1:4523/export/openapi/10?version=3.0")
    inputSpec.set(File(layout.projectDirectory.asFile, "openapi.json").path)
    outputDir.set(File(layout.buildDirectory.get().asFile, "generated").path)

    cleanupOutput.set(true)
}

tasks {
    register<Copy>("copyCli") {
        group = "build"
        description = "Copies the OpenAPI Generator CLI to the build directory"
        from(project.configurations.runtimeClasspath)
        include("openapi-generator-cli-*.jar")
        into(File(layout.buildDirectory.get().asFile, "libs"))
    }

    build {
        dependsOn("copyCli")
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType(GeneratorsTask::class) {
//        dependsOn("copyCli")
    }

}
