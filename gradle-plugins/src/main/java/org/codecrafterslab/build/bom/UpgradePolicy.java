package org.codecrafterslab.build.bom;

import org.codecrafterslab.build.bom.version.DependencyVersion;

import java.util.function.BiPredicate;

/**
 * Policies used to decide which versions are considered as possible upgrades.
 */
public enum UpgradePolicy implements BiPredicate<DependencyVersion, DependencyVersion> {

    /**
     * Any version.
     */
    ANY((candidate, current) -> true),

    /**
     * Minor versions of the current major version.
     */
    SAME_MAJOR_VERSION(DependencyVersion::isSameMajor),

    /**
     * Patch versions of the current minor version.
     */
    SAME_MINOR_VERSION(DependencyVersion::isSameMinor);

    private final BiPredicate<DependencyVersion, DependencyVersion> delegate;

    UpgradePolicy(BiPredicate<DependencyVersion, DependencyVersion> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(DependencyVersion candidate, DependencyVersion current) {
        return this.delegate.test(candidate, current);
    }

}
