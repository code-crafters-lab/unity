//package io.github.coffee377.gradle.plugin.base;
//
//import com.voc.gradle.plugin.VersionResolutionAction;
//import org.gradle.api.Plugin;
//import org.gradle.api.Project;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//
///**
// * @author WuYujie
// * @email coffee377@dingtalk.com
// * @time 2022/11/06 22:46
// */
//public class DependencyVersionPlugin implements Plugin<Project> {
//    @Override
//    public void apply(@NotNull Project project) {
////        DependencyManagementExtension managementExtension = project.getExtensions().getByType
// (DependencyManagementExtension.class);
//        VersionResolutionAction versionResolutionAction = new VersionResolutionAction(project, new HashMap<>());
//        project.getConfigurations().all(configuration ->
//                configuration.resolutionStrategy(
//                        resolutionStrategy -> resolutionStrategy.eachDependency(versionResolutionAction)
//                )
//        );
//    }
//}
