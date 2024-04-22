package org.codecrafterslab;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ProjectInfoBase implements ProjectInfo, Comparable<ProjectInfoBase> {
    private Project project;
    private boolean root;
    private File dir;
    private File buildFile;
    private String path;
    private String name;

    public ProjectInfoBase(File buildFile) {
        this.buildFile = buildFile;
        this.dir = buildFile.getParentFile();
    }

    public ProjectInfoBase(Project project) {
        this.project = project;
        this.dir = project.getProjectDir();
    }

    @Override
    public boolean isRootProject() {
        return project.getRootProject().getPath() == "";
    }

    @Override
    public File getDir() {
        return dir;
    }

    @Override
    public String getBuildFileName() {
        return buildFile.getName();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NotNull ProjectInfoBase o) {
        return this.path.compareTo(o.path);
    }
}
