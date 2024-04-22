package org.codecrafterslab.build.bom;

import org.codecrafterslab.build.bom.lib.Group;
import org.codecrafterslab.build.bom.lib.LibraryVersion;
import org.codecrafterslab.build.bom.lib.ProhibitedVersion;
import org.codecrafterslab.build.bom.lib.VersionAlignment;
import org.codecrafterslab.build.bom.lib.handler.LibraryHandler;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * A collection of modules, Maven plugins, and Maven bom that are versioned
 * and released together.
 */
public class Library {

    private final String name;

    private final String calendarName;

    private final LibraryVersion version;

    private final List<Group> groups;

    private final String versionProperty;

    private final List<ProhibitedVersion> prohibitedVersions;

    private final boolean considerSnapshots;

    private final VersionAlignment versionAlignment;

    private final String linkRootName;

    private final Map<String, Function<LibraryVersion, String>> links;

    /**
     * Create a new {@code Library} with the given {@code name}, {@code version}, and
     * {@code groups}.
     *
     * @param name               name of the library
     * @param calendarName       name of the library as it appears in the Spring Calendar. May
     *                           be {@code null} in which case the {@code name} is used.
     * @param version            version of the library
     * @param groups             groups in the library
     * @param prohibitedVersions version of the library that are prohibited
     * @param considerSnapshots  whether to consider snapshots
     * @param versionAlignment   version alignment, if any, for the library
     * @param linkRootName       the root name to use when generating link variable or
     *                           {@code null} to generate one based on the library {@code name}
     * @param links              a list of HTTP links relevant to the library
     */
    public Library(String name, String calendarName, LibraryVersion version, List<Group> groups,
                   List<ProhibitedVersion> prohibitedVersions, boolean considerSnapshots, VersionAlignment versionAlignment,
                   String linkRootName, Map<String, Function<LibraryVersion, String>> links) {
        this.name = name;
        this.calendarName = (calendarName != null) ? calendarName : name;
        this.version = version;
        this.groups = groups;
        this.versionProperty = StringUtils.hasText(name) ? name.toLowerCase(Locale.ENGLISH).replace(' ', '-') + ".version" : null;
        this.prohibitedVersions = prohibitedVersions;
        this.considerSnapshots = considerSnapshots;
        this.versionAlignment = versionAlignment;
        this.linkRootName = (linkRootName != null) ? linkRootName : generateLinkRootName(name);
        this.links = Collections.unmodifiableMap(links);
    }

    public Library(String name, LibraryHandler libraryHandler, LibraryVersion libraryVersion, VersionAlignment versionAlignment) {
        this(name, libraryHandler.getCalendarName(), libraryVersion, libraryHandler.getGroups(), libraryHandler.getProhibitedVersions(), libraryHandler.isConsiderSnapshots(), versionAlignment, libraryHandler.getLinkRootName(), libraryHandler.getLinks());
    }

    private static String generateLinkRootName(String name) {
        return name.replace("-", "").replace(" ", "-").toLowerCase();
    }

    public String getName() {
        return this.name;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public LibraryVersion getVersion() {
        return this.version;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public String getVersionProperty() {
        return this.versionProperty;
    }

    public List<ProhibitedVersion> getProhibitedVersions() {
        return this.prohibitedVersions;
    }

    public boolean isConsiderSnapshots() {
        return this.considerSnapshots;
    }

    public VersionAlignment getVersionAlignment() {
        return this.versionAlignment;
    }

    public String getLinkRootName() {
        return this.linkRootName;
    }

    public Map<String, String> getLinks() {
        Map<String, String> links = new TreeMap<>();
        this.links.forEach((name, linkFactory) -> links.put(name, linkFactory.apply(this.version)));
        return Collections.unmodifiableMap(links);
    }


}
