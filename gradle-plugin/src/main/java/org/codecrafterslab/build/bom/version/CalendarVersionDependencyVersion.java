package org.codecrafterslab.build.bom.version;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.regex.Pattern;

/**
 * A specialization of {@link ArtifactVersionDependencyVersion} for calendar versions.
 * Calendar versions are always considered to be newer than
 * {@link ReleaseTrainDependencyVersion release train versions}.
 *
 * @author Andy Wilkinson
 */
class CalendarVersionDependencyVersion extends ArtifactVersionDependencyVersion {

    private static final Pattern CALENDAR_VERSION_PATTERN = Pattern.compile("\\d{4}\\.\\d+\\.\\d+(-.+)?");

    protected CalendarVersionDependencyVersion(ArtifactVersion artifactVersion) {
        super(artifactVersion);
    }

    protected CalendarVersionDependencyVersion(ArtifactVersion artifactVersion, ComparableVersion comparableVersion) {
        super(artifactVersion, comparableVersion);
    }

    static CalendarVersionDependencyVersion parse(String version) {
        if (!CALENDAR_VERSION_PATTERN.matcher(version).matches()) {
            return null;
        }
        ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
        if (artifactVersion.getQualifier() != null && artifactVersion.getQualifier().equals(version)) {
            return null;
        }
        return new CalendarVersionDependencyVersion(artifactVersion);
    }

}
