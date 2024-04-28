package io.github.coffee377.gradle.utils;

import org.gradle.api.Project;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/10/24 20:09
 */
public class ProjectUtils {
    private ProjectUtils() {
    }

    /**
     * 获取项目名称
     *
     * @param project 项目
     * @return 项目名
     */
    public static String getProjectName(Project project) {
        String projectName = project.getRootProject().getName();
        if (projectName.endsWith("-build")) {
            projectName = projectName.substring(0, projectName.length() - "-build".length());
        }
        return projectName;
    }

    /**
     * 是否快照版本
     *
     * @param project 项目
     * @return true for the snapshot version, otherwise false
     */
    public static boolean isSnapshot(Project project) {
        String projectVersion = projectVersion(project);
        return projectVersion.matches("^.*([.-]BUILD)?-SNAPSHOT$");
    }

    /**
     * 是否里程碑版本
     *
     * @param project 项目
     * @return boolean
     */
    public static boolean isMilestone(Project project) {
        String projectVersion = projectVersion(project);
        return projectVersion.matches("^.*[.-]M\\d+$") || projectVersion.matches("^.*[.-]RC\\d+$");
    }

    /**
     * 是否发布版本
     *
     * @param project 项目
     * @return boolean
     */
    public static boolean isRelease(Project project) {
        return !(isSnapshot(project) || isMilestone(project));
    }

    /**
     * 获取项目版本
     *
     * @param project 项目
     * @return 版本字符串
     */
    private static String projectVersion(Project project) {
        return String.valueOf(project.getVersion());
    }
}
