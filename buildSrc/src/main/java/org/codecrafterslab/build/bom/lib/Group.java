package org.codecrafterslab.build.bom.lib;

import org.codecrafterslab.build.bom.lib.handler.LibraryHandler;

import java.util.List;

/**
 * A collection of modules, Maven plugins, and Maven boms with the same group ID.
 */
public class Group {

    private final String id;

    private final List<Module> modules;

    private final List<String> plugins;

    private final List<String> boms;

    public Group(String id, List<Module> modules, List<String> plugins, List<String> boms) {
        this.id = id;
        this.modules = modules;
        this.plugins = plugins;
        this.boms = boms;
    }

    public Group(LibraryHandler.GroupHandler handler) {
        this(handler.getId(), handler.getModules(), handler.getPlugins(), handler.getImports());
    }

    public String getId() {
        return this.id;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public List<String> getPlugins() {
        return this.plugins;
    }

    public List<String> getBoms() {
        return this.boms;
    }

}
