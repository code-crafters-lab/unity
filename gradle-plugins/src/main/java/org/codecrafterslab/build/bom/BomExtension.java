package org.codecrafterslab.build.bom;

import org.codecrafterslab.build.bom.lib.Group;
import org.codecrafterslab.build.bom.lib.LibraryVersion;
import org.codecrafterslab.build.bom.lib.Module;
import org.codecrafterslab.build.bom.lib.VersionAlignment;
import org.codecrafterslab.build.bom.lib.handler.LibraryHandler;
import org.codecrafterslab.build.bom.lib.handler.UpgradeHandler;
import org.codecrafterslab.build.bom.version.DependencyVersion;
import org.codecrafterslab.gradle.plugins.dependency.ManagementPlugin;
import org.gradle.api.Action;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.tasks.TaskExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.*;


/**
 * DSL extensions for {@link BomPlugin}.
 */
public class BomExtension {
    private final DependencyHandler dependencyHandler;
    private final Project project;
    private final UpgradeHandler upgradeHandler;

    private final Map<String, DependencyVersion> properties = new LinkedHashMap<>();
    private final Map<String, String> artifactVersionProperties = new HashMap<>();
    private final List<Library> libraries = new ArrayList<>();


    public BomExtension(DependencyHandler dependencyHandler, Project project) {
        this.dependencyHandler = dependencyHandler;
        this.project = project;
        this.upgradeHandler = project.getObjects().newInstance(UpgradeHandler.class);
    }

    public List<Library> getLibraries() {
        return this.libraries;
    }

    public void upgrade(Action<UpgradeHandler> action) {
        action.execute(this.upgradeHandler);
    }

    public Upgrade getUpgrade() {
        UpgradeHandler upgradeHandler = this.upgradeHandler;
        return new Upgrade(upgradeHandler);
    }

    public void library(String name, Action<LibraryHandler> action) {
        library(name, null, action);
    }

    public void library(String name, String version, Action<LibraryHandler> action) {
        ObjectFactory objects = this.project.getObjects();
        LibraryHandler libraryHandler = objects.newInstance(LibraryHandler.class, (version != null) ? version : "");
        action.execute(libraryHandler);
        LibraryVersion libraryVersion = new LibraryVersion(DependencyVersion.parse(libraryHandler.getVersion()));
        VersionAlignment versionAlignment = (libraryHandler.getAlignWithVersion() != null) ? new VersionAlignment(this.project, this.libraries, libraryHandler, libraryHandler.getAlignWithVersion()) : null;
        addLibrary(new Library(name, libraryHandler, libraryVersion, versionAlignment));
    }

    public void effectiveBomArtifact() {
//        Configuration effectiveBomConfiguration = this.project.getConfigurations().create("effectiveBom");
//        this.project.getTasks()
//                .matching((task) -> task.getName().equals(DeployedPlugin.GENERATE_POM_TASK_NAME))
//                .all((task) -> {
//                    Sync syncBom = this.project.getTasks().create("syncBom", Sync.class);
//                    syncBom.dependsOn(task);
//                    File generatedBomDir = new File(this.project.getBuildDir(), "generated/bom");
//                    syncBom.setDestinationDir(generatedBomDir);
//                    syncBom.from(((GenerateMavenPom) task).getDestination(), (pom) -> pom.rename((name) -> "pom.xml"));
//                    try {
//                        String settingsXmlContent = FileCopyUtils
//                                .copyToString(new InputStreamReader(
//                                        getClass().getClassLoader().getResourceAsStream("effective-bom-settings.xml"),
//                                        StandardCharsets.UTF_8))
//                                .replace("localRepositoryPath",
//                                        new File(this.project.getBuildDir(), "local-m2-repository").getAbsolutePath());
//                        syncBom.from(this.project.getResources().getText().fromString(settingsXmlContent),
//                                (settingsXml) -> settingsXml.rename((name) -> "settings.xml"));
//                    } catch (IOException ex) {
//                        throw new GradleException("Failed to prepare settings.xml", ex);
//                    }
//                    MavenExec generateEffectiveBom = this.project.getTasks()
//                            .create("generateEffectiveBom", MavenExec.class);
//                    generateEffectiveBom.setProjectDir(generatedBomDir);
//                    File effectiveBom = new File(this.project.getBuildDir(),
//                            "generated/effective-bom/" + this.project.getName() + "-effective-bom.xml");
//                    generateEffectiveBom.args("--settings", "settings.xml", "help:effective-pom",
//                            "-Doutput=" + effectiveBom);
//                    generateEffectiveBom.dependsOn(syncBom);
//                    generateEffectiveBom.getOutputs().file(effectiveBom);
//                    generateEffectiveBom.doLast(new StripUnrepeatableOutputAction(effectiveBom));
//                    this.project.getArtifacts()
//                            .add(effectiveBomConfiguration.getName(), effectiveBom,
//                                    (artifact) -> artifact.builtBy(generateEffectiveBom));
//                });
    }

    private String createDependencyNotation(String groupId, String artifactId, DependencyVersion version) {
        return groupId + ":" + artifactId + ":" + version;
    }

    Map<String, DependencyVersion> getProperties() {
        return this.properties;
    }

    String getArtifactVersionProperty(String groupId, String artifactId, String classifier) {
        String coordinates = groupId + ":" + artifactId + ":" + classifier;
        return this.artifactVersionProperties.get(coordinates);
    }

    private void putArtifactVersionProperty(String groupId, String artifactId, String versionProperty) {
        putArtifactVersionProperty(groupId, artifactId, null, versionProperty);
    }

    private void putArtifactVersionProperty(String groupId, String artifactId, String classifier, String versionProperty) {
        String coordinates = groupId + ":" + artifactId + ":" + ((classifier != null) ? classifier : "");
        String existing = this.artifactVersionProperties.putIfAbsent(coordinates, versionProperty);
        if (existing != null) {
            throw new InvalidUserDataException("Cannot put version property for '" + coordinates + "'. Version property '" + existing + "' has already been stored.");
        }
    }

    private void addLibrary(Library library) {
        this.libraries.add(library);
        String versionProperty = library.getVersionProperty();
        if (versionProperty != null) {
            this.properties.put(versionProperty, library.getVersion().getVersion());
        }
        for (Group group : library.getGroups()) {
            for (Module module : group.getModules()) {
                putArtifactVersionProperty(group.getId(), module.getName(), module.getClassifier(), versionProperty);
                this.dependencyHandler.getConstraints().add(JavaPlatformPlugin.API_CONFIGURATION_NAME, createDependencyNotation(group.getId(), module.getName(), library.getVersion().getVersion()));
            }
            for (String bomImport : group.getBoms()) {
                putArtifactVersionProperty(group.getId(), bomImport, versionProperty);
                String bomDependency = createDependencyNotation(group.getId(), bomImport, library.getVersion().getVersion());
                this.dependencyHandler.add(JavaPlatformPlugin.API_CONFIGURATION_NAME, this.dependencyHandler.platform(bomDependency));
                this.dependencyHandler.add(ManagementPlugin.MANAGEMENT_CONFIGURATION_NAME,
                        this.dependencyHandler.enforcedPlatform(bomDependency));
            }
        }
    }

    public static final class Upgrade {

        private final UpgradePolicy upgradePolicy;

        private final GitHub gitHub;

        private Upgrade(UpgradePolicy upgradePolicy, GitHub gitHub) {
            this.upgradePolicy = upgradePolicy;
            this.gitHub = gitHub;
        }

        private Upgrade(UpgradeHandler upgradeHandler) {
            this(upgradeHandler.getUpgradePolicy(), new GitHub(upgradeHandler.getGitHub()));
        }

        public UpgradePolicy getPolicy() {
            return this.upgradePolicy;
        }

        public GitHub getGitHub() {
            return this.gitHub;
        }

    }

    public static final class GitHub {

        private String organization = "spring-projects";

        private String repository = "spring-boot";

        private final List<String> issueLabels;

        private GitHub(String organization, String repository, List<String> issueLabels) {
            this.organization = organization;
            this.repository = repository;
            this.issueLabels = issueLabels;
        }

        private GitHub(UpgradeHandler.GitHubHandler gitHubHandler) {
            this(gitHubHandler.getOrganization(), gitHubHandler.getRepository(), gitHubHandler.getIssueLabels());
        }


        public String getOrganization() {
            return this.organization;
        }

        public String getRepository() {
            return this.repository;
        }

        public List<String> getIssueLabels() {
            return this.issueLabels;
        }

    }

    private static final class StripUnrepeatableOutputAction implements Action<Task> {

        private final File effectiveBom;

        private StripUnrepeatableOutputAction(File xmlFile) {
            this.effectiveBom = xmlFile;
        }

        @Override
        public void execute(Task task) {
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.effectiveBom);
                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList comments = (NodeList) xpath.evaluate("//comment()", document, XPathConstants.NODESET);
                for (int i = 0; i < comments.getLength(); i++) {
                    org.w3c.dom.Node comment = comments.item(i);
                    comment.getParentNode().removeChild(comment);
                }
                org.w3c.dom.Node build = (org.w3c.dom.Node) xpath.evaluate("/project/build", document, XPathConstants.NODE);
                build.getParentNode().removeChild(build);
                org.w3c.dom.Node reporting = (org.w3c.dom.Node) xpath.evaluate("/project/reporting", document, XPathConstants.NODE);
                reporting.getParentNode().removeChild(reporting);
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(this.effectiveBom));
            } catch (Exception ex) {
                throw new TaskExecutionException(task, ex);
            }
        }

    }

}
