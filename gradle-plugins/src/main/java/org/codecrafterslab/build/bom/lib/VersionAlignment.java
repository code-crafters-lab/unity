package org.codecrafterslab.build.bom.lib;

import org.codecrafterslab.build.bom.Library;
import org.codecrafterslab.build.bom.lib.handler.LibraryHandler;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.result.DependencyResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Version alignment for a library.
 */
public class VersionAlignment {

    private final String from;

    private final String managedBy;

    private final Project project;

    private final List<Group> groups;

    private final List<Library> libraries;

    private Set<String> alignedVersions;

    public VersionAlignment(String from, String managedBy, Project project, List<Library> libraries, List<Group> groups) {
        this.from = from;
        this.managedBy = managedBy;
        this.project = project;
        this.groups = groups;
        this.libraries = libraries;
    }

    public VersionAlignment(Project project, List<Library> libraries, LibraryHandler libraryHandler, LibraryHandler.AlignWithVersionHandler versionHandler) {
        this(versionHandler.getFrom(), versionHandler.getManagedBy(), project, libraries, libraryHandler.getGroups());
    }

    public Set<String> resolve() {
        if (this.managedBy == null) {
            throw new IllegalStateException("Version alignment without managedBy is not supported");
        }
        if (this.alignedVersions != null) {
            return this.alignedVersions;
        }
        Library managingLibrary = this.libraries.stream()
                .filter((candidate) -> this.managedBy.equals(candidate.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Managing library '" + this.managedBy + "' not found."));
        Map<String, String> versions = resolveAligningDependencies(managingLibrary);
        Set<String> versionsInLibrary = new HashSet<>();
        for (Group group : this.groups) {
            for (Module module : group.getModules()) {
                String version = versions.get(group.getId() + ":" + module.getName());
                if (version != null) {
                    versionsInLibrary.add(version);
                }
            }
            for (String plugin : group.getPlugins()) {
                String version = versions.get(group.getId() + ":" + plugin);
                if (version != null) {
                    versionsInLibrary.add(version);
                }
            }
        }
        this.alignedVersions = versionsInLibrary;
        return this.alignedVersions;
    }

    private Map<String, String> resolveAligningDependencies(Library manager) {
        DependencyHandler dependencyHandler = this.project.getDependencies();
        List<Dependency> dependencies = manager.getGroups()
                .stream()
                .flatMap((group) -> group.getBoms()
                        .stream()
                        .map((bom) -> dependencyHandler
                                .platform(group.getId() + ":" + bom + ":" + manager.getVersion().getVersion())))
                .collect(Collectors.toList());
        dependencies.add(dependencyHandler.create(this.from));
        Configuration alignmentConfiguration = this.project.getConfigurations()
                .detachedConfiguration(dependencies.toArray(new Dependency[0]));
        Map<String, String> versions = new HashMap<>();
        for (DependencyResult dependency : alignmentConfiguration.getIncoming()
                .getResolutionResult()
                .getAllDependencies()) {
            ModuleVersionIdentifier moduleVersion = dependency.getFrom().getModuleVersion();
            if (moduleVersion != null) {
                versions.put(moduleVersion.getModule().toString(), moduleVersion.getVersion());
            }

        }
        return versions;
    }

    String getFrom() {
        return this.from;
    }

    String getManagedBy() {
        return this.managedBy;
    }

    @Override
    public String toString() {
        return "version from dependencies of " + this.from + " that is managed by " + this.managedBy;
    }

}
