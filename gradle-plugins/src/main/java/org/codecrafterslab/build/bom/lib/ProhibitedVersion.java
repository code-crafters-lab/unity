package org.codecrafterslab.build.bom.lib;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.List;

/**
 * A version or range of versions that are prohibited from being used in a bom.
 */
public class ProhibitedVersion {

    private final VersionRange range;

    private final List<String> startsWith;

    private final List<String> endsWith;

    private final List<String> contains;

    private final String reason;

    public ProhibitedVersion(VersionRange range, List<String> startsWith, List<String> endsWith, List<String> contains, String reason) {
        this.range = range;
        this.startsWith = startsWith;
        this.endsWith = endsWith;
        this.contains = contains;
        this.reason = reason;
    }

    public VersionRange getRange() {
        return this.range;
    }

    public List<String> getStartsWith() {
        return this.startsWith;
    }

    public List<String> getEndsWith() {
        return this.endsWith;
    }

    public List<String> getContains() {
        return this.contains;
    }

    public String getReason() {
        return this.reason;
    }

    public boolean isProhibited(String candidate) {
        boolean result = this.range != null && this.range.containsVersion(new DefaultArtifactVersion(candidate));
        result = result || this.startsWith.stream().anyMatch(candidate::startsWith);
        result = result || this.endsWith.stream().anyMatch(candidate::endsWith);
        result = result || this.contains.stream().anyMatch(candidate::contains);
        return result;
    }

}