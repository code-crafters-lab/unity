package io.github.coffee377.gradle.plugin.maven;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @since 0.1.0
 */
public class PublishNexusPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().withType(MavenPublishPlugin.class, (mavenPublish) -> {
            PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
            publishing.getRepositories().maven((maven) -> {
                maven.setName("JinQi");
                maven.setUrl("http://nexus.jqk8s.jqsoft.net/repository/maven-releases/");
                maven.setAllowInsecureProtocol(true);
                maven.credentials(credentials -> {
                    String username = System.getenv("DEV_OPTS_JQ_USERNAME");
                    String password = System.getenv("DEV_OPTS_JQ_PASSWORD");

                    credentials.setUsername(username);
                    credentials.setPassword(password);
                });
            });
        });
    }
}
