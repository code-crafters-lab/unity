package org.codecrafterslab.build.bom;//package build.bom;
//
//import org.gradle.api.DefaultTask;
//import org.gradle.api.GradleException;
//import org.gradle.api.tasks.TaskAction;
//
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.stream.Collectors;
//
//import static build.bom.Library.*;
//
///**
// * Checks the validity of a bom.
// *
// * @author Andy Wilkinson
// */
//public class CheckBom extends DefaultTask {
//
//    private final BomExtension bom;
//
//    @Inject
//    public CheckBom(BomExtension bom) {
//        this.bom = bom;
//    }
//
//    @TaskAction
//    void checkBom() {
//        List<String> errors = new ArrayList<>();
//        for (Library library : this.bom.getLibraries()) {
//            checkLibrary(library, errors);
//        }
//        if (!errors.isEmpty()) {
//            System.out.println();
//            errors.forEach(System.out::println);
//            System.out.println();
//            throw new GradleException("Bom check failed. See previous output for details.");
//        }
//    }
//
//    private void checkLibrary(Library library, List<String> errors) {
//        List<String> libraryErrors = new ArrayList<>();
//        checkExclusions(library, libraryErrors);
//        checkProhibitedVersions(library, libraryErrors);
//        checkVersionAlignment(library, libraryErrors);
//        if (!libraryErrors.isEmpty()) {
//            errors.add(library.getName());
//            for (String libraryError : libraryErrors) {
//                errors.add("    - " + libraryError);
//            }
//        }
//    }
//
//    private void checkExclusions(Library library, List<String> errors) {
//        for (Group group : library.getGroups()) {
//            for (Module module : group.getModules()) {
//                if (!module.getExclusions().isEmpty()) {
//                    checkExclusions(group.getId(), module, library.getVersion().getVersion(), errors);
//                }
//            }
//        }
//    }
//
//    private void checkExclusions(String groupId, Module module, DependencyVersion version, List<String> errors) {
//        Set<String> resolved = getProject().getConfigurations()
//                .detachedConfiguration(
//                        getProject().getDependencies().create(groupId + ":" + module.getName() + ":" + version))
//                .getResolvedConfiguration()
//                .getResolvedArtifacts()
//                .stream()
//                .map((artifact) -> artifact.getModuleVersion().getId())
//                .map((id) -> id.getGroup() + ":" + id.getModule().getName())
//                .collect(Collectors.toSet());
//        Set<String> exclusions = module.getExclusions()
//                .stream()
//                .map((exclusion) -> exclusion.getGroupId() + ":" + exclusion.getArtifactId())
//                .collect(Collectors.toSet());
//        Set<String> unused = new TreeSet<>();
//        for (String exclusion : exclusions) {
//            if (!resolved.contains(exclusion)) {
//                if (exclusion.endsWith(":*")) {
//                    String group = exclusion.substring(0, exclusion.indexOf(':') + 1);
//                    if (resolved.stream().noneMatch((candidate) -> candidate.startsWith(group))) {
//                        unused.add(exclusion);
//                    }
//                } else {
//                    unused.add(exclusion);
//                }
//            }
//        }
//        exclusions.removeAll(resolved);
//        if (!unused.isEmpty()) {
//            errors.add("Unnecessary exclusions on " + groupId + ":" + module.getName() + ": " + exclusions);
//        }
//    }
//
//    private void checkProhibitedVersions(Library library, List<String> errors) {
//        ArtifactVersion currentVersion = new DefaultArtifactVersion(library.getVersion().getVersion().toString());
//        for (ProhibitedVersion prohibited : library.getProhibitedVersions()) {
//            if (prohibited.isProhibited(library.getVersion().getVersion().toString())) {
//                errors.add("Current version " + currentVersion + " is prohibited");
//            } else {
//                VersionRange versionRange = prohibited.getRange();
//                if (versionRange != null) {
//                    for (Restriction restriction : versionRange.getRestrictions()) {
//                        ArtifactVersion upperBound = restriction.getUpperBound();
//                        if (upperBound == null) {
//                            return;
//                        }
//                        int comparison = currentVersion.compareTo(upperBound);
//                        if ((restriction.isUpperBoundInclusive() && comparison <= 0)
//                                || ((!restriction.isUpperBoundInclusive()) && comparison < 0)) {
//                            return;
//                        }
//                    }
//                    errors.add("Version range " + versionRange + " is ineffective as the current version, "
//                            + currentVersion + ", is greater than its upper bound");
//                }
//            }
//        }
//    }
//
//    private void checkVersionAlignment(Library library, List<String> errors) {
//        VersionAlignment versionAlignment = library.getVersionAlignment();
//        if (versionAlignment == null) {
//            return;
//        }
//        Set<String> alignedVersions = versionAlignment.resolve();
//        if (alignedVersions.size() == 1) {
//            String alignedVersion = alignedVersions.iterator().next();
//            if (!alignedVersion.equals(library.getVersion().getVersion().toString())) {
//                errors.add("Version " + library.getVersion().getVersion() + " is misaligned. It should be "
//                        + alignedVersion + ".");
//            }
//        } else {
//            if (alignedVersions.isEmpty()) {
//                errors.add("Version alignment requires a single version but none were found.");
//            } else {
//                errors.add("Version alignment requires a single version but " + alignedVersions.size() + " were
//                found: "
//                        + alignedVersions + ".");
//            }
//        }
//    }
//
//}
