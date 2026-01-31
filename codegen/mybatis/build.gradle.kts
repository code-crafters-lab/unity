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

tasks {

    register<Delete>("clear") {
        group = "codegen"
        description = "Clean generated code"
        delete = setOf(File(layout.buildDirectory.get().asFile, "generated"))
    }

    register<Copy>("copyMybatisCode") {
        group = "codegen"
        description = "Copy generated code to mybatis project"
        from(sourceSets.main.get().java.srcDirs) {
            include("net/jqsoft/**/*.java")
        }
        into(File("/Users/wuyujie/Project/jqsoft/teamwork/cds-bid", "src/main/java"))
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

}

