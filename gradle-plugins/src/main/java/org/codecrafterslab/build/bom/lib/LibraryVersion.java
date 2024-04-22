package org.codecrafterslab.build.bom.lib;

import org.codecrafterslab.build.bom.version.DependencyVersion;

import java.util.Arrays;

public class LibraryVersion {

    private final DependencyVersion version;

    public LibraryVersion(DependencyVersion version) {
        this.version = version;
    }

    public DependencyVersion getVersion() {
        return this.version;
    }

    public int[] componentInts() {
        return Arrays.stream(parts()).mapToInt(Integer::parseInt).toArray();
    }

    public String major() {
        return parts()[0];
    }

    public String minor() {
        return parts()[1];
    }

    public String patch() {
        return parts()[2];
    }

    @Override
    public String toString() {
        return this.version.toString();
    }

    public String toString(String separator) {
        return this.version.toString().replace(".", separator);
    }

    public String forAntora() {
        String[] parts = parts();
        String result = parts[0] + "." + parts[1];
        if (toString().endsWith("SNAPSHOT")) {
            result += "-SNAPSHOT";
        }
        return result;
    }

    private String[] parts() {
        return toString().split("[.-]");
    }

}

