package org.codecrafterslab.build.bom.lib.handler;

import org.codecrafterslab.build.bom.UpgradePolicy;
import org.gradle.api.Action;

import java.util.List;

public class UpgradeHandler {

    private UpgradePolicy upgradePolicy;

    private final GitHubHandler gitHub = new GitHubHandler();

    public UpgradePolicy getUpgradePolicy() {
        return upgradePolicy;
    }

    public void setUpgradePolicy(UpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }

    public GitHubHandler getGitHub() {
        return gitHub;
    }

    public void gitHub(Action<GitHubHandler> action) {
        action.execute(this.gitHub);
    }

    public static class GitHubHandler {

        private String organization = "spring-projects";

        private String repository = "spring-boot";

        private List<String> issueLabels;

        public List<String> getIssueLabels() {
            return issueLabels;
        }

        public String getOrganization() {
            return organization;
        }

        public String getRepository() {
            return repository;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }

        public void setIssueLabels(List<String> issueLabels) {
            this.issueLabels = issueLabels;
        }

    }
}
