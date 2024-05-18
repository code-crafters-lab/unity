package org.codecrafterslab.unity.dict.boot.provider;

@FunctionalInterface
public interface EnumDictProviderBuilderCustomizer {
    void customize(EnumDictProviderBuilder enumDictProviderBuilder);
}
