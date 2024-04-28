plugins {
    id("com.voc.lib")
    id("com.voc.publish")
}

configurations {
//    create("jre8") {
//        isCanBeConsumed = true
//        isCanBeResolved = false
//        attributes {
//            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
//            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
//            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
//            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, JavaVersion.VERSION_1_8.majorVersion.toInt())
//            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES_AND_RESOURCES))
//        }
//        // If you want this configuration to share the same dependencies, otherwise omit this line
//        extendsFrom(configurations["implementation"], configurations["runtimeOnly"])
//    }
}

dependencies {
}


