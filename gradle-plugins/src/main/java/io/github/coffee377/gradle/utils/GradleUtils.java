package io.github.coffee377.gradle.utils;

import org.gradle.api.GradleException;
import org.gradle.util.GradleVersion;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @time 2022/10/27 17:04
 */
public class GradleUtils {

    public static final String MIN_GRADLE_VERSION = "7.4";

    public static void verifyGradleVersion() {
        GradleVersion currentVersion = GradleVersion.current();
        if (currentVersion.compareTo(GradleVersion.version(MIN_GRADLE_VERSION)) < 0) {
            throw new GradleException("Devtools plugin requires Gradle" + MIN_GRADLE_VERSION
                    + " The current version is " + currentVersion);
        }
    }
}
