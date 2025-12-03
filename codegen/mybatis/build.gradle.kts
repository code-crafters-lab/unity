
plugins {
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.mybatis.generator:mybatis-generator-core:1.4.2")
    implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")
    implementation("com.mysql:mysql-connector-j:8.4.0")
    compileOnly("org.mybatis:mybatis:3.5.19")
    compileOnly("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    annotationProcessor(libs.lombok)
}

tasks {
    register<JavaExec>("listGenerators") {
        group = "codegen"
        description = "List OpenAPI Generators"

        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("org.openapitools.codegen.OpenAPIGenerator")
        args = listOf("list")
    }

    register<Delete>("clear") {
        group = "codegen"
        description = "Clean generated code"
        delete = setOf(File(layout.buildDirectory.get().asFile, "generated"))
    }

    register<JavaExec>("generate") {
        group = "codegen"
        description = "Generate Code"

        dependsOn("clear")

        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("org.openapitools.codegen.OpenAPIGenerator")

        args(
            "generate", "-g", "controller", "-i", "http://127.0.0.1:4523/export/openapi/5?version=3.0",
            "-o",
            layout.buildDirectory.dir("generated").get().asFile
        )

        debugOptions {
//            enabled = true
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

    build {
        dependsOn("copyCli")
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

//    withType(GenerateTask::class) {
//        dependsOn(build, "copyCli")
//
//    }
//
//    withType(GeneratorsTask::class) {
//        dependsOn(build, "copyCli")
//    }

}

