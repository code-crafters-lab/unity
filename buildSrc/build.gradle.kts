import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.util.StringUtils
import java.io.FileInputStream
import java.io.IOException
import java.util.*

plugins {
    idea
    `java-library`
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}
apply {
    plugin(InnerGradlePlugin::class.java)
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

repositories {
    mavenLocal()
    maven {
        url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
        isAllowInsecureProtocol = true
    }
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
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
        create("base") {
            id = "com.voc.base"
            implementationClass = "org.codecrafterslab.gradle.BasePlugin"
        }
        create("grpc") {
            id = "com.voc.grpc"
            implementationClass = "org.codecrafterslab.gradle.GRPCPlugin"
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
        create("JinQiSoftNexus3") {
            id = "net.jqsoft.nexus3"
            implementationClass = "net.jqsoft.Nexus3Plugin"
        }
    }

}

tasks {
    register("lib", Copy::class.java) {
        group = "build"
        from(configurations.runtimeClasspath)
        into("${layout.buildDirectory.get().asFile.path}/lib")
    }
}

class InnerGradlePlugin : Plugin<Project> {
    private val props = Properties()

    override fun apply(project: Project) {
        syncConfig(project)
        configureClean(project)
        configureCompile(project)
        configureJar(project)
        configureJavadoc(project)
        configureAnnotationProcessor(project)
        configurePublish(project)
        configureTest(project)
    }

    private fun syncConfig(project: Project) {
        if (project.version == Project.DEFAULT_VERSION && project.projectDir.name == "buildSrc") {
            listOf("gradle.properties", "gradle.local.properties").forEach {
                try {
                    FileInputStream(File(project.projectDir.parent, it)).use { input ->
                        props.load(input)
                    }
                } catch (ignored: IOException) {
                }
            }

            val version = props.getProperty("version")
            if (StringUtils.hasText(version)) {
                project.version = version
            }
        }
    }

    private fun configureTest(project: Project) {
        project.tasks.withType(Test::class.java) {
            useJUnitPlatform()
        }
    }

    /**
     * 清理任务增加删除目录
     *
     * @param project Project
     */
    private fun configureClean(project: Project) {/* 清理任务增加删除目录 */
        project.tasks.withType(Delete::class.java) {
            delete("out", "build")
        }
    }

    /**
     * 编译配置
     *
     * @param project Project
     */
    private fun configureCompile(project: Project) {
        project.tasks.withType(JavaCompile::class.java) {
            if (options.encoding == null) {
                options.encoding = "UTF-8"
            }
            options.compilerArgs.add("-parameters")
            options.release.set(17)
        }
        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    /**
     * 配置打包策略
     *
     * @param project Project
     */
    private fun configureJar(project: Project) {
        project.tasks.withType(Jar::class.java) {
            /* 重复文件策略，排除 */
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            includeEmptyDirs = false
            exclude("rebel.xml")
            manifest {
                val attributes: MutableMap<String, String> = HashMap()
                attributes["Created-By"] = String.format(
                    "%s (%s)", System.getProperty("java.version"), System.getProperty("java.specification.vendor")
                )
                attributes["Implementation-Title"] = project.name
                attributes["Implementation-Version"] = project.version.toString()
                attributes["Automatic-Module-Name"] = project.name.replace("-", ".")
                attributes(attributes)
            }
        }
    }

    private fun configureJavadoc(project: Project) {
        project.tasks.withType(Javadoc::class.java) {
            options.encoding("UTF-8")
            if (options is StandardJavadocDocletOptions) {
                val standardJavadocDocletOptions = options as StandardJavadocDocletOptions
                standardJavadocDocletOptions.charSet("UTF-8")
                standardJavadocDocletOptions.addStringOption("Xdoclint:none", "-quiet")
                standardJavadocDocletOptions.tags?.add("email")
                standardJavadocDocletOptions.tags?.add("time")
            }
        }
    }

    private fun configureAnnotationProcessor(project: Project) {
        val configurations = project.configurations
        val annotationConfiguration = if (!project.pluginManager.hasPlugin("java")) {/* 不存 java 插件时，自己手动创建一个，保证兼容使用 */
            project.configurations.create(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
            project.configurations.create(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        } else {
            configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        }

        val testAnnotationConfiguration =
            configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        testAnnotationConfiguration.extendsFrom(annotationConfiguration)

        val compileOnlyConfiguration = configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        compileOnlyConfiguration.extendsFrom(annotationConfiguration)

        val testCompileOnlyConfiguration = configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME)
        testCompileOnlyConfiguration.extendsFrom(compileOnlyConfiguration, testAnnotationConfiguration)
    }

    private fun configurePublish(project: Project) {
        project.extensions.configure(PublishingExtension::class.java) {
            repositories.maven {
                name = "JinQiSoftNexus3"
                url = project.uri("http://nexus.jqk8s.jqsoft.net/repository/maven-${getVersionType(project)}/")
                isAllowInsecureProtocol = true
                credentials {
                    username = props.getProperty("dev.opts.nexus.username", "dev")
                    password = props.getProperty("dev.opts.nexus.password", "dev")
                }
            }
        }
    }

    /**
     * @see <a href="https://semver.org">Semantic Versioning</a>
     * @since 0.4.0
     */
    private fun getVersionType(project: Project): String {
        val matchers = Regex(
            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-(" + "(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        ).find(project.version.toString())

        /* pre-release 部分 */
        val preRelease: String = matchers?.groups?.get(4)?.value ?: ""

        val versionType = when {
            preRelease == "" -> "releases"
            Regex("^(M|RC|beta).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "prerelease"
            Regex("^(alpha|SNAPSHOT).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "snapshots"
            else -> "snapshots"
        }
        return versionType
    }
}

