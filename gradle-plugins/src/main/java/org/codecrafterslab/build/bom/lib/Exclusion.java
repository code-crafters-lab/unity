package org.codecrafterslab.build.bom.lib;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An exclusion of a dependency identified by its group ID and artifact ID.
 */
public class Exclusion {

    private final String groupId;

    private final String artifactId;

    public Exclusion(String ga) {
        Assert.hasLength(ga, "Exclusion 项不能为空");
        if ("*".equals(ga)) ga = "*:*";
        String[] split = ga.split(":");
        this.groupId = StringUtils.hasText(split[0]) ? split[0] : "*";
        this.artifactId = StringUtils.hasText(split[1]) ? split[1] : "*";
    }

    public Exclusion(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

}