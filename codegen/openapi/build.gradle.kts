import org.openapitools.generator.gradle.plugin.tasks.GeneratorsTask
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.openapi.generator") version "7.14.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(dependencyNotation = libs.openapi.generator) {
        exclude("org.slf4j", "slf4j-simple")
    }
    annotationProcessor(libs.google.auto.service)
    annotationProcessor(libs.lombok)
    runtimeOnly(libs.openapi.generator.cli)
}

openApiGenerate {
    verbose.set(false)
    generatorName.set("java")
    inputSpec.set(File(layout.projectDirectory.asFile, "openapi.json").path)
    outputDir.set(File(layout.buildDirectory.get().asFile, "generated").path)

    cleanupOutput.set(true)
}

tasks {
    register<JavaExec>("listGenerators") {
        group = "codegen"
        description = "List OpenAPI Generators"

        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("org.openapitools.codegen.OpenAPIGenerator")
        args = listOf("list")
    }

    register<JavaExec>("generate") {
        group = "codegen"
        description = "Generate Code"

        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("org.openapitools.codegen.OpenAPIGenerator")

        args(
            "generate", "-g", "controller", "-i", "http://127.0.0.1:4523/export/openapi/11?version=3.0",
            "-o",
            layout.buildDirectory.dir("generated").get().asFile
        )

        debugOptions {
            enabled = true
            port = 5005
            server = true
            suspend = true
        }
    }

    register<Copy>("copyCli") {
        group = "codegen"
        description = "Copies the OpenAPI Generator CLI to the build directory"
        from(project.configurations.runtimeClasspath)
        include("openapi-generator-cli-*.jar")
        into(File(layout.buildDirectory.get().asFile, "libs"))
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType(GenerateTask::class) {
        dependsOn(build, "copyCli")

    }

    withType(GeneratorsTask::class) {
        dependsOn(build, "copyCli")
    }

}

