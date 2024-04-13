package org.codecrafterslab.build.bom.lib.handler;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.codecrafterslab.build.bom.lib.Module;
import org.codecrafterslab.build.bom.lib.*;
import org.gradle.api.Action;
import org.gradle.api.InvalidUserCodeException;
import org.gradle.api.InvalidUserDataException;
import org.springframework.util.PropertyPlaceholderHelper;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;

public class LibraryHandler {

    private final List<Group> groups = new ArrayList<>();

    private final List<ProhibitedVersion> prohibitedVersions = new ArrayList<>();

    private boolean considerSnapshots = false;

    private String version;

    private String calendarName;

    private AlignWithVersionHandler alignWithVersion;

    private String linkRootName;

    private final Map<String, Function<LibraryVersion, String>> links = new HashMap<>();

    public List<Group> getGroups() {
        return groups;
    }

    public List<ProhibitedVersion> getProhibitedVersions() {
        return prohibitedVersions;
    }

    public boolean isConsiderSnapshots() {
        return considerSnapshots;
    }

    public String getVersion() {
        return version;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public AlignWithVersionHandler getAlignWithVersion() {
        return alignWithVersion;
    }

    public String getLinkRootName() {
        return linkRootName;
    }

    public Map<String, Function<LibraryVersion, String>> getLinks() {
        return links;
    }

    @Inject
    public LibraryHandler(String version) {
        this.version = version;
    }

    public void version(String version) {
        this.version = version;
    }

    public void considerSnapshots() {
        this.considerSnapshots = true;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public void group(String id, Action<GroupHandler> action) {
        GroupHandler groupHandler = new GroupHandler(id);
        action.execute(groupHandler);
        this.groups.add(new Group(groupHandler));
    }

    public void prohibit(Action<ProhibitedHandler> action) {
        ProhibitedHandler handler = new ProhibitedHandler();
        action.execute(handler);
        this.prohibitedVersions.add(new ProhibitedVersion(handler.versionRange, handler.startsWith, handler.endsWith, handler.contains, handler.reason));
    }

    public void alignWithVersion(Action<AlignWithVersionHandler> action) {
        this.alignWithVersion = new AlignWithVersionHandler();
        action.execute(this.alignWithVersion);
    }

    public void links(Action<LinksHandler> action) {
        links(null, action);
    }

    public void links(String linkRootName, Action<LinksHandler> action) {
        LinksHandler handler = new LinksHandler();
        action.execute(handler);
        this.linkRootName = linkRootName;
        this.links.putAll(handler.links);
    }

    public static class ProhibitedHandler {

        private String reason;

        private final List<String> startsWith = new ArrayList<>();

        private final List<String> endsWith = new ArrayList<>();

        private final List<String> contains = new ArrayList<>();

        private VersionRange versionRange;

        public void versionRange(String versionRange) {
            try {
                this.versionRange = VersionRange.createFromVersionSpec(versionRange);
            } catch (InvalidVersionSpecificationException ex) {
                throw new InvalidUserCodeException("Invalid version range", ex);
            }
        }

        public void startsWith(String startsWith) {
            this.startsWith.add(startsWith);
        }

        public void startsWith(Collection<String> startsWith) {
            this.startsWith.addAll(startsWith);
        }

        public void endsWith(String endsWith) {
            this.endsWith.add(endsWith);
        }

        public void endsWith(Collection<String> endsWith) {
            this.endsWith.addAll(endsWith);
        }

        public void contains(String contains) {
            this.contains.add(contains);
        }

        public void contains(List<String> contains) {
            this.contains.addAll(contains);
        }

        public void because(String because) {
            this.reason = because;
        }

    }

    public static class GroupHandler extends GroovyObjectSupport {

        final String id;

        List<Module> modules = new ArrayList<>();

        List<String> imports = new ArrayList<>();

        List<String> plugins = new ArrayList<>();

        public String getId() {
            return id;
        }

        public List<String> getImports() {
            return imports;
        }

        public List<Module> getModules() {
            return modules;
        }

        public List<String> getPlugins() {
            return plugins;
        }

        public GroupHandler(String id) {
            this.id = id;
        }

        public void setModules(List<Object> modules) {
            this.modules = modules.stream().map((input) -> (input instanceof Module module) ? module : new Module((String) input)).toList();
        }

        public void setImports(List<String> imports) {
            this.imports = imports;
        }

        public void setPlugins(List<String> plugins) {
            this.plugins = plugins;
        }

        public Object methodMissing(String name, Object args) {
            if (args instanceof Object[] && ((Object[]) args).length == 1) {
                Object arg = ((Object[]) args)[0];
                if (arg instanceof Closure<?> closure) {
                    ModuleHandler moduleHandler = new ModuleHandler();
                    closure.setResolveStrategy(Closure.DELEGATE_FIRST);
                    closure.setDelegate(moduleHandler);
                    closure.call(moduleHandler);
                    return new Module(name, moduleHandler.type, moduleHandler.classifier, moduleHandler.exclusions);
                }
            }
            throw new InvalidUserDataException("Invalid configuration for module '" + name + "'");
        }

        public static class ModuleHandler {

            private final List<Exclusion> exclusions = new ArrayList<>();

            private String type;

            private String classifier;

            public void exclude(Map<String, String> exclusion) {
                this.exclusions.add(new Exclusion(exclusion.get("group"), exclusion.get("module")));
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setClassifier(String classifier) {
                this.classifier = classifier;
            }

        }

    }

    public static class AlignWithVersionHandler {

        private String from;

        private String managedBy;

        public String getFrom() {
            return from;
        }

        public String getManagedBy() {
            return managedBy;
        }

        public void from(String from) {
            this.from = from;
        }

        public void managedBy(String managedBy) {
            this.managedBy = managedBy;
        }

    }

    public static class LinksHandler {

        private final Map<String, Function<LibraryVersion, String>> links = new HashMap<>();

        public void site(String linkTemplate) {
            site(asFactory(linkTemplate));
        }

        public void site(Function<LibraryVersion, String> linkFactory) {
            add("site", linkFactory);
        }

        public void github(String linkTemplate) {
            github(asFactory(linkTemplate));
        }

        public void github(Function<LibraryVersion, String> linkFactory) {
            add("github", linkFactory);
        }

        public void docs(String linkTemplate) {
            docs(asFactory(linkTemplate));
        }

        public void docs(Function<LibraryVersion, String> linkFactory) {
            add("docs", linkFactory);
        }

        public void javadoc(String linkTemplate) {
            javadoc(asFactory(linkTemplate));
        }

        public void javadoc(Function<LibraryVersion, String> linkFactory) {
            add("javadoc", linkFactory);
        }

        public void releaseNotes(String linkTemplate) {
            releaseNotes(asFactory(linkTemplate));
        }

        public void releaseNotes(Function<LibraryVersion, String> linkFactory) {
            add("releaseNotes", linkFactory);
        }

        public void add(String name, String linkTemplate) {
            add(name, asFactory(linkTemplate));
        }

        public void add(String name, Function<LibraryVersion, String> linkFactory) {
            this.links.put(name, linkFactory);
        }

        private Function<LibraryVersion, String> asFactory(String linkTemplate) {
            return (version) -> {
                PropertyPlaceholderHelper.PlaceholderResolver resolver = (name) -> "version".equals(name) ? version.toString() : null;
                return new PropertyPlaceholderHelper("{", "}").replacePlaceholders(linkTemplate, resolver);
            };
        }

    }

}
