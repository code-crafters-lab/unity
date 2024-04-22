package org.codecrafterslab.build.bom;

import groovy.namespace.QName;
import groovy.util.Node;
import org.codecrafterslab.build.PublishPlugin;
import org.codecrafterslab.build.bom.lib.Group;
import org.codecrafterslab.build.bom.lib.Module;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlatformExtension;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.MavenPublication;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link Plugin} for defining a bom. Dependencies are added as constraints in the
 * {@code api} configuration. Imported boms are added as enforced platforms in the
 * {@code api} configuration.
 *
 * @author Andy Wilkinson
 */
public class BomPlugin implements Plugin<Project> {

    public static final String BOM_EXTENSION_NAME = "bom";
    static final String API_ENFORCED_CONFIGURATION_NAME = "apiEnforced";

    @Override
    public void apply(Project project) {
        PluginContainer plugins = project.getPlugins();
        plugins.apply(PublishPlugin.class);
        plugins.apply(JavaPlatformPlugin.class);
        JavaPlatformExtension javaPlatform = project.getExtensions().getByType(JavaPlatformExtension.class);
        javaPlatform.allowDependencies();
        createApiEnforcedConfiguration(project);
        BomExtension bom = project.getExtensions().create(BOM_EXTENSION_NAME, BomExtension.class,
                project.getDependencies(), project);
//        CheckBom checkBom = project.getTasks().create("bomrCheck", CheckBom.class, bom);
//        project.getTasks().named("check").configure((check) -> check.dependsOn(checkBom));
//        project.getTasks().create("bomrUpgrade", UpgradeBom.class, bom);
//        project.getTasks().create("moveToSnapshots", MoveToSnapshots.class, bom);
//        project.getTasks().register("checkLinks", CheckLinks.class, bom);
        new PublishingCustomizer(project, bom).customize();
    }

    private void createApiEnforcedConfiguration(Project project) {
        Configuration apiEnforced = project.getConfigurations().create(API_ENFORCED_CONFIGURATION_NAME,
                (configuration) -> {
            configuration.setCanBeConsumed(false);
            configuration.setCanBeResolved(false);
            configuration.setVisible(false);
        });
        project.getConfigurations().getByName(JavaPlatformPlugin.ENFORCED_API_ELEMENTS_CONFIGURATION_NAME).extendsFrom(apiEnforced);
        project.getConfigurations().getByName(JavaPlatformPlugin.ENFORCED_RUNTIME_ELEMENTS_CONFIGURATION_NAME).extendsFrom(apiEnforced);
    }

    private static final class PublishingCustomizer {

        private final Project project;

        private final BomExtension bom;

        private PublishingCustomizer(Project project, BomExtension bom) {
            this.project = project;
            this.bom = bom;
        }

        private void customize() {
            PublishingExtension publishing = this.project.getExtensions().getByType(PublishingExtension.class);
            publishing.getPublications().withType(MavenPublication.class).all(this::configurePublication);
        }

        private void configurePublication(MavenPublication publication) {
            publication.pom(this::customizePom);
        }

        @SuppressWarnings("unchecked")
        private void customizePom(MavenPom pom) {
            pom.withXml((xml) -> {
                Node projectNode = xml.asNode();
                Node properties = new Node(null, "properties");
                /* 创建 properties 节点 */
                this.bom.getProperties().forEach(properties::appendNode);
                Node dependencyManagement = findChild(projectNode, "dependencyManagement");
                if (dependencyManagement != null) {
                    /* 将 properties 节点添加到 dependencyManagement 节点之前 */
                    addPropertiesBeforeDependencyManagement(projectNode, properties);
                    addClassifiedManagedDependencies(dependencyManagement);
                    replaceVersionsWithVersionPropertyReferences(dependencyManagement);
                    addExclusionsToManagedDependencies(dependencyManagement);
                    addTypesToManagedDependencies(dependencyManagement);
                } else {
                    projectNode.children().add(properties);
                }
                addPluginManagement(projectNode);
            });
        }

        @SuppressWarnings("unchecked")
        private void addPropertiesBeforeDependencyManagement(Node projectNode, Node properties) {
            for (int i = 0; i < projectNode.children().size(); i++) {
                if (isNodeWithName(projectNode.children().get(i), "dependencyManagement")) {
                    projectNode.children().add(i, properties);
                    break;
                }
            }
        }

        private void replaceVersionsWithVersionPropertyReferences(Node dependencyManagement) {
            Node dependencies = findChild(dependencyManagement, "dependencies");
            if (dependencies != null) {
                for (Node dependency : findDependency(dependencies)) {
                    String groupId = Objects.requireNonNull(findChild(dependency, "groupId")).text();
                    String artifactId = Objects.requireNonNull(findChild(dependency, "artifactId")).text();
                    Node classifierNode = findChild(dependency, "classifier");
                    String classifier = (classifierNode != null) ? classifierNode.text() : "";
                    String versionProperty = this.bom.getArtifactVersionProperty(groupId, artifactId, classifier);
                    if (versionProperty != null) {
                        Node versionNode = findChild(dependency, "version");
                        if (versionNode == null) {
                            dependency.appendNode("version", "${" + versionProperty + "}");
                        } else {
                            versionNode.setValue("${" + versionProperty + "}");
                        }
                    }
                }
            }
        }

        private void addExclusionsToManagedDependencies(Node dependencyManagement) {
            Node dependencies = findChild(dependencyManagement, "dependencies");
            if (dependencies != null) {
                for (Node dependency : findDependency(dependencies)) {
                    String groupId = Objects.requireNonNull(findChild(dependency, "groupId")).text();
                    String artifactId = Objects.requireNonNull(findChild(dependency, "artifactId")).text();
                    this.bom.getLibraries().stream().flatMap((library) -> library.getGroups().stream()).filter((group) -> group.getId().equals(groupId)).flatMap((group) -> group.getModules().stream()).filter((module) -> module.getName().equals(artifactId)).flatMap((module) -> module.getExclusions().stream()).forEach((exclusion) -> {
                        Node exclusions = findOrCreateNode(dependency, "exclusions");
                        Node node = new Node(exclusions, "exclusion");
                        node.appendNode("groupId", exclusion.getGroupId());
                        node.appendNode("artifactId", exclusion.getArtifactId());
                    });
                }
            }
        }

        private void addTypesToManagedDependencies(Node dependencyManagement) {
            Node dependencies = findChild(dependencyManagement, "dependencies");
            if (dependencies != null) {
                for (Node dependency : findDependency(dependencies)) {
                    String groupId = Objects.requireNonNull(findChild(dependency, "groupId")).text();
                    String artifactId = Objects.requireNonNull(findChild(dependency, "artifactId")).text();
                    Set<String> types = this.getModuleMapper(groupId, artifactId, Module::getType);
                    if (types.size() > 1) {
                        throw new IllegalStateException("Multiple types for " + groupId + ":" + artifactId + ": " + types);
                    }
                    if (types.size() == 1) {
                        String type = types.iterator().next();
                        dependency.appendNode("type", type);
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void addClassifiedManagedDependencies(Node dependencyManagement) {
            Node dependencies = findChild(dependencyManagement, "dependencies");
            if (dependencies != null) {
                for (Node dependency : findDependency(dependencies)) {
                    String groupId = Objects.requireNonNull(findChild(dependency, "groupId")).text();
                    String artifactId = Objects.requireNonNull(findChild(dependency, "artifactId")).text();
                    String version = Objects.requireNonNull(findChild(dependency, "version")).text();
                    Set<String> classifiers = getModuleMapper(groupId, artifactId, Module::getClassifier);
                    Node target = dependency;
                    for (String classifier : classifiers) {
                        if (!classifier.isEmpty()) {
                            if (target == null) {
                                target = new Node(null, "dependency");
                                target.appendNode("groupId", groupId);
                                target.appendNode("artifactId", artifactId);
                                target.appendNode("version", version);
                                int index = dependency.parent().children().indexOf(dependency);
                                dependency.parent().children().add(index + 1, target);
                            }
                            target.appendNode("classifier", classifier);
                        }
                        target = null;
                    }
                }
            }
        }

        private @NotNull Set<String> getModuleMapper(String groupId, String artifactId,
                                                     Function<Module, String> mapper) {
            return this.bom.getLibraries().stream().flatMap((library) -> library.getGroups().stream()).filter((group) -> group.getId().equals(groupId)).flatMap((group) -> group.getModules().stream()).filter((module) -> module.getName().equals(artifactId)).map(mapper).filter(Objects::nonNull).collect(Collectors.toSet());
        }


        private static class PluginInfo {
            Library library;
            String groupId;
            String artifactId;
            String versionProperty;
            String version;

            public PluginInfo(Library library, Group group, String plugin) {
                this.library = library;
                this.versionProperty = library.getVersionProperty();
                this.version = library.getVersion().getVersion().toString();
                this.groupId = group.getId();
                this.artifactId = plugin;
            }

        }

        private void addPluginManagement(Node projectNode) {
            this.addPluginManagement(projectNode, this.bom.getLibraries());
        }

        private void addPluginManagement(Node projectNode, List<Library> libraries) {
            List<PluginInfo> pluginInfos = this.getPlugins(libraries);
            if (pluginInfos.isEmpty()) return;
            Node plugins = findOrCreateNode(projectNode, "build", "pluginManagement", "plugins");
            pluginInfos.forEach(pluginInfo -> {
                Node plugin = new Node(plugins, "plugin");
                plugin.appendNode("groupId", pluginInfo.groupId);
                plugin.appendNode("artifactId", pluginInfo.artifactId);
                String version = pluginInfo.version;
                if (StringUtils.hasText(pluginInfo.versionProperty)) {
                    version = "${" + pluginInfo.versionProperty + "}";
                }
                plugin.appendNode("version", version);
                // todo 增加插件配置
            });
        }

        private List<PluginInfo> getPlugins(List<Library> libraries) {
            return libraries.stream()
                    .flatMap(library -> library.getGroups().stream()
                            .flatMap(group -> group.getPlugins().stream()
                                    .map(pluginName -> new PluginInfo(library, group, pluginName))
                            )
                    )
                    .collect(Collectors.toList());
        }

        private Node findOrCreateNode(Node parent, String... path) {
            Node current = parent;
            for (String nodeName : path) {
                Node child = findChild(current, nodeName);
                if (child == null) {
                    child = new Node(current, nodeName);
                }
                current = child;
            }
            return current;
        }

        private Node findChild(Node parent, String name) {
            for (Object child : parent.children()) {
                if (child instanceof Node node) {
                    if ((node.name() instanceof QName qname) && name.equals(qname.getLocalPart())) {
                        return node;
                    }
                    if (name.equals(node.name())) {
                        return node;
                    }
                }
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private List<Node> findDependency(Node parent) {
            return parent.children().stream().filter((child) -> isNodeWithName(child, "dependency")).toList();
        }

        private boolean isNodeWithName(Object candidate, String name) {
            if (candidate instanceof Node node) {
                if ((node.name() instanceof QName qname) && name.equals(qname.getLocalPart())) {
                    return true;
                }
                return name.equals(node.name());
            }
            return false;
        }

    }

}
