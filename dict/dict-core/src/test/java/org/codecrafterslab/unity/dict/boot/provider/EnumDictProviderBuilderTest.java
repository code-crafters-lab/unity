package org.codecrafterslab.unity.dict.boot.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumDictProviderBuilderTest {

    @Test
    void enumDictProviderBuilder() {
        EnumDictProviderBuilder builder = new EnumDictProviderBuilder();
        builder.packages(packages -> {
                    packages.add("org.codecrafterslab.unity");
                    packages.add("org.codecrafterslab.unity");
                })
                .scan(EnumDictProviderBuilderTest.class.getPackage().getName())
                .exclude(EnumDictProviderBuilderTest.class)
                .build();

        assertEquals(2, builder.getScanPackages().size());
        assertEquals(1, builder.getExcludeFilters().size());
    }
}
