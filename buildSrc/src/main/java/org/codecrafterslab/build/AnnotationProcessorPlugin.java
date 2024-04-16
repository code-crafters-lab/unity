package org.codecrafterslab.build;

import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.Delete;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.external.javadoc.StandardJavadocDocletOptions;
import org.gradle.jvm.tasks.Jar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationProcessorPlugin implements Plugin<Project> {
    private static final Logger log = LoggerFactory.getLogger(AnnotationProcessorPlugin.class);

    @Override
    public void apply(@NotNull Project project) {
        configureAnnotationProcessor(project);
        configureJavaCompile(project);
        configureJar(project);
        configureClean(project);
        project.getPlugins().withType(JavaPlugin.class, (javaPlugin) -> {
//            SourceSetContainer sourceSets = project.getExtensions()
//                    .getByType(JavaPluginExtension.class)
//                    .getSourceSets();
//            sourceSets.all((sourceSet) -> {
//                project.getConfigurations()
//                        .getByName(sourceSet.getCompileClasspathConfigurationName())
//                        .extendsFrom(annotationConfiguration);
//                project.getConfigurations()
//                        .getByName(sourceSet.getRuntimeClasspathConfigurationName())
//                        .extendsFrom(annotationConfiguration);
//            });
        });
    }

    /**
     * 清理任务增加删除目录
     *
     * @param project Project
     */
    private void configureClean(Project project) {
        /* 清理任务增加删除目录 */
        project.getTasks().withType(Delete.class, delete -> delete.delete("out", "build"));
    }

    /**
     * 配置打包策略
     *
     * @param project Project
     */
    private void configureJar(Project project) {
        project.getTasks().withType(Jar.class, jar -> {
            /* 重复文件策略，排除 */
            jar.setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE);
            jar.setIncludeEmptyDirs(false);
            /* 排除 JRebel 配置文件 */
            jar.exclude("rebel.xml");
        });
    }

    /**
     * 编译配置
     *
     * @param project Project
     */
    private void configureJavaCompile(Project project) {
        project.afterEvaluate((evaluated) -> evaluated.getTasks().withType(JavaCompile.class, (compile) -> {
            CompileOptions options = compile.getOptions();
            if (options.getEncoding() == null) {
                options.setEncoding("UTF-8");
            }
            options.getCompilerArgs().add("-parameters");
            if (JavaVersion.current().isJava8Compatible()) {
                options.getRelease().set(8);
            } else if (JavaVersion.current().isJava11Compatible()) {
                options.getRelease().set(11);
            }
//            project.getLogger().warn(" <<<<<< {} >>>>>>", options.getRelease().get());

//                    FileCollection annotationProcessorPath = options.getAnnotationProcessorPath();
//
//                    Configuration configuration = evaluated.getConfigurations().getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME);
//                    if (annotationProcessorPath != null) {
//                        annotationProcessorPath.plus(configuration);
//                    } else {
//                        options.setAnnotationProcessorPath(configuration);
//                    }
        }));
    }

    /**
     * 注解处理器配置
     *
     * @param project Project
     */
    private void configureAnnotationProcessor(Project project) {
        ConfigurationContainer configurations = project.getConfigurations();

        Configuration annotationConfiguration = configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME);
        Configuration testAnnotationConfiguration = configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME);
        testAnnotationConfiguration.extendsFrom(annotationConfiguration);

        Configuration compileOnlyConfiguration = configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME);
        compileOnlyConfiguration.extendsFrom(annotationConfiguration);

        Configuration testCompileOnlyConfiguration = configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME);
        testCompileOnlyConfiguration.extendsFrom(compileOnlyConfiguration, testAnnotationConfiguration);
    }

    /**
     * 注解处理器配置添加到编译环境 & 依赖缓存时间配置
     *
     * @param project Project
     */
    public void configureConfigurations(Project project) {
        project.getTasks().withType(Javadoc.class, new Action<Javadoc>() {
            @Override
            public void execute(@NotNull Javadoc javadoc) {
                javadoc.options(options -> {
                    options.encoding("UTF-8");
                    if (options instanceof StandardJavadocDocletOptions) {
                        ((StandardJavadocDocletOptions) options).charSet("UTF-8");
                    }
                });
            }
        });

        ConfigurationContainer configurations = project.getConfigurations();

//        Configuration runtimeOnlyConfiguration = configurations.getByName(JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME);
//        Configuration testRuntimeOnlyConfiguration = configurations.getByName(JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME);
//        testRuntimeOnlyConfiguration.extendsFrom(runtimeOnlyConfiguration);
//
//        Configuration runtimeClasspathConfiguration = configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME);
//        Configuration testRuntimeClasspathConfiguration = configurations.getByName(JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
//        testRuntimeClasspathConfiguration.extendsFrom(runtimeClasspathConfiguration);

        configurations.all(configuration -> configuration.resolutionStrategy(resolutionStrategy -> {
            /* 动态版本依赖缓存 10 minutes */
//            resolutionStrategy.cacheDynamicVersionsFor(10, TimeUnit.MINUTES);
            /* SNAPSHOT版本依赖缓存 0 seconds */
//            resolutionStrategy.cacheChangingModulesFor(10, TimeUnit.SECONDS);
        }));
    }

}
