import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.openapitools.generator.gradle.plugin.tasks.GeneratorsTask

plugins {
//    alias(libs.plugins.openapi.generator)
    id("org.openapi.generator") version "7.14.0"
}

dependencies {
    api(platform(libs.junit.bom))

    implementation(libs.openapi.generator)
//    implementation(libs.openapi.generator.cli)

    annotationProcessor(libs.google.auto.service)
    annotationProcessor(libs.lombok)

//    runtimeOnly("org.openapitools:openapi-generator-cli")
//    runtimeOnly(files(layout.buildDirectory.file("libs/codegen-openapi-1.0.0-SNAPSHOT.jar")))
//
    testImplementation("org.junit.jupiter:junit-jupiter")
}

openApiMeta {
    generatorName.set("Sample")
    outputFolder.set("${layout.buildDirectory.get().asFile}/generators/my-codegen")
}

openApiValidate {
    inputSpec.set(File(layout.buildDirectory.get().asFile, "petstore-v3.0-invalid.yaml").path)
    recommend.set(true)
}

openApiGenerate {
    verbose.set(false)
    skipValidateSpec.set(true)
//    engine.set("handlebars")
    generatorName.set("typescript-fetch")
    cleanupOutput.set(true)
    generateApiDocumentation.set(false)
//    inputSpec.set("${layout.projectDirectory.asFile}/openapi.json")
    inputSpec.set(File(layout.projectDirectory.asFile, "petstore-v3.0.yaml").path)
    outputDir.set(File(layout.buildDirectory.get().asFile, "generated").path)

}

openApiGenerators {

}


tasks {
    register<Copy>("copyCli") {
        mustRunAfter(build)
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

    withType(GeneratorsTask::class) {
        dependsOn(build, "copyCli")
    }

    withType(GenerateTask::class) {
        dependsOn(build, "copyCli")
        configurations.runtimeClasspath.configure {
        }
    }


}
