package org.codecrafterslab.gradle.plugins.api;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency;

/**
 * @author Wu Yujie
 * @since 0.7.0
 */
public interface DependencyAware extends ProjectAware {


    /**
     * 添加依赖
     *
     * @param configurationName {@link Configuration#getName()} 配置名称
     * @param dependency        {@link Dependency} 依赖
     */
    default void addDependency(String configurationName, Dependency dependency) {
        DependencyHandler dependencies = getProject().getDependencies();
        dependencies.add(configurationName, dependency);
    }

    /**
     * 添加依赖
     *
     * @param configurationName  配置名称
     * @param dependencyNotation 依赖
     */
    default void addDependency(String configurationName, Object dependencyNotation) {
        DependencyHandler dependencies = getProject().getDependencies();
        Dependency dependency = dependencies.create(dependencyNotation);
        dependencies.add(configurationName, dependency);
    }

//    default void addDependency(String configurationName, MinimalExternalModuleDependency minimalDependency) {
//        DependencyHandler dependencies = getProject().getDependencies();
//        ExternalModuleDependencyFactory dependencyFactory = new DefaultExternalDependencyFactory(null, null);
//        Provider<MinimalExternalModuleDependency> minimalExternalModuleDependencyProvider = dependencyFactory.create(
//                "");
//        Dependency dependency = dependencies.create(minimalExternalModuleDependencyProvider);
//        dependencies.add(configurationName, dependency);
//    }

    default void addDependency(String configurationName, ModuleIdentifier moduleIdentifier,
                               MutableVersionConstraint versionConstraint) {
        addDependency(configurationName, new DefaultMinimalDependency(moduleIdentifier, versionConstraint));
    }


}
