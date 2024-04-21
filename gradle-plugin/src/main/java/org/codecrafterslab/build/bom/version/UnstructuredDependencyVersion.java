package org.codecrafterslab.build.bom.version;

import org.apache.maven.artifact.versioning.ComparableVersion;

/**
 * A {@link DependencyVersion} with no structure such that version comparisons are not
 * possible.
 *
 * @author Andy Wilkinson
 */
final class UnstructuredDependencyVersion extends AbstractDependencyVersion implements DependencyVersion {

    private final String version;

    private UnstructuredDependencyVersion(String version) {
        super(new ComparableVersion(version));
        this.version = version;
    }

    @Override
    public boolean isSameMajor(DependencyVersion other) {
        return true;
    }

    @Override
    public boolean isSameMinor(DependencyVersion other) {
        return true;
    }

    @Override
    public String toString() {
        return this.version;
    }

    @Override
    public boolean isSnapshotFor(DependencyVersion candidate) {
        return false;
    }

    static UnstructuredDependencyVersion parse(String version) {
        return new UnstructuredDependencyVersion(version);
    }

}
