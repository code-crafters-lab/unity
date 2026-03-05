plugins {
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.mybatis.generator:mybatis-generator-core:1.4.2")
    implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")
    implementation("com.mysql:mysql-connector-j:8.4.0")
    implementation("org.springframework:spring-core:5.3.39")
    implementation("ch.qos.logback:logback-classic:1.5.21")
    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.43.0")
    implementation("org.eclipse.platform:org.eclipse.text:3.14.400")

    compileOnly("org.mybatis:mybatis:3.5.19")
    compileOnly("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    annotationProcessor(libs.lombok)
}
val baseDir = "/Users/wuyujie/Project/jqsoft/teamwork"

tasks {

    register<Delete>("bid-clear") {
        group = "codegen"
        description = "clean bid generated code"
        delete = setOf(
            File(
                layout.buildDirectory.get().asFile.parentFile,
                String.format("src/main/java/net/jqsoft/%s", "cds/bid")
            )
        )
    }

    register<JavaExec>("bid-gen") {
        dependsOn("bid-clear")
        group = "codegen"
        mainClass.set("org.codecrafterslab.unity.codegen.mybatis.Generator")
        classpath = sourceSets.main.get().runtimeClasspath
        args = listOf("bid")
    }

    register<Copy>("bid") {
        dependsOn("bid-gen")
        group = "codegen"
        description = "copy generated code to mybatis project"
        from(sourceSets.main.get().java.srcDirs) {
            include("net/jqsoft/cds/bid/**/*.java")
        }
        into(File("${baseDir}/cds-bid", "src/main/java"))
    }

    register<Delete>("task-clear") {
        group = "codegen"
        description = "clean task generated code"
        delete = setOf(
            File(
                layout.buildDirectory.get().asFile.parentFile,
                String.format("src/main/java/net/jqsoft/%s", "cds/task")
            )
        )
    }

    register<JavaExec>("task-gen") {
        dependsOn("task-clear")
        group = "codegen"
        mainClass.set("org.codecrafterslab.unity.codegen.mybatis.Generator")
        classpath = sourceSets.main.get().runtimeClasspath
        args = listOf("task")
    }

    register<Copy>("task") {
        dependsOn("task-gen")
        group = "codegen"
        description = "copy generated code to mybatis project"
        from(sourceSets.main.get().java.srcDirs) {
            include("net/jqsoft/cds/task/**/*.java")
        }
        into(File("${baseDir}/cds-bid", "src/main/java"))
    }

    build {
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

}

