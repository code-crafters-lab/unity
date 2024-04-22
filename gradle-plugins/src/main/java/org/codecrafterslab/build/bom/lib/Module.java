package org.codecrafterslab.build.bom.lib;

import java.util.Collections;
import java.util.List;

/**
 * A module in a group.
 */
public class Module {

    private final String name;

    private final String type;

    private final String classifier;

    private final List<Exclusion> exclusions;

    /**
     * 是否可选
     */
    private boolean optional;

    /**
     * 模块范围
     */
    private String scope;


    public Module(String name) {
        this(name, Collections.emptyList());
    }

    public Module(String name, String type) {
        this(name, type, null, Collections.emptyList());
    }

    public Module(String name, List<Exclusion> exclusions) {
        this(name, null, null, exclusions);
    }

    public Module(String name, String type, String classifier, List<Exclusion> exclusions) {
        this.name = name;
        this.type = type;
        this.classifier = (classifier != null) ? classifier : "";
        this.exclusions = exclusions;
    }

    public String getName() {
        return this.name;
    }

    public String getClassifier() {
        return this.classifier;
    }

    public String getType() {
        return this.type;
    }

    public List<Exclusion> getExclusions() {
        return this.exclusions;
    }

}