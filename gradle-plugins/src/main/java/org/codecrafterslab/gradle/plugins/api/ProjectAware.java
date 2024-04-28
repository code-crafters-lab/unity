package org.codecrafterslab.gradle.plugins.api;

import org.gradle.api.Project;

/**
 * @author Wu Yujie
 * @since 0.7.0
 */
public interface ProjectAware {

    /**
     * 获取当前项目
     *
     * @return Project
     */
    Project getProject();

}
