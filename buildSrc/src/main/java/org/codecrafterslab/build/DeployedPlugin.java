package org.codecrafterslab.build;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.bundling.Jar;

/**
 * 需要发布到 maven 仓库的项目使用此插件
 */
@Deprecated
public class DeployedPlugin implements Plugin<Project> {

    /**
     * Name of the task that generates the deployed pom file.
     */
    public static final String GENERATE_POM_TASK_NAME = "generatePomFileForMavenPublication";

    public static final String MAVEN_PUBLICATION_NAME = "maven";

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(MavenRepositoryPlugin.class);
        PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
        MavenPublication mavenPublication = publishing.getPublications().create(MAVEN_PUBLICATION_NAME,
                MavenPublication.class);
        project.afterEvaluate((evaluated) -> {
                    project.getPlugins().withType(JavaPlugin.class).all((javaPlugin) -> {
                        if (((Jar) project.getTasks().getByName(JavaPlugin.JAR_TASK_NAME)).isEnabled()) {
                            project.getComponents()
                                    .matching((component) -> component.getName().equals("java"))
                                    .all(mavenPublication::from);
                        }
                    });

                }
        );
        project.getPlugins().withType(JavaPlatformPlugin.class).all((javaPlugin) ->
                project.getComponents()
                        .matching((component) -> component.getName().equals("javaPlatform"))
                        .all(mavenPublication::from)
        );

    }

}
