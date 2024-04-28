package io.github.coffee377.gradle.plugin.docs;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.external.javadoc.MinimalJavadocOptions;
import org.gradle.external.javadoc.StandardJavadocDocletOptions;
import org.jetbrains.annotations.NotNull;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/11/04 17:06
 */
public class JavadocOptionsPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project project) {
        project.getTasks().withType(Javadoc.class, (javadoc) -> {
            MinimalJavadocOptions options = javadoc.getOptions();
            options.encoding("UTF-8");
            if (options instanceof StandardJavadocDocletOptions) {
                StandardJavadocDocletOptions opts = (StandardJavadocDocletOptions) options;
                opts.charSet("UTF-8");
                opts.addStringOption("Xdoclint:none", "-quiet");
            }
        });
    }
}
