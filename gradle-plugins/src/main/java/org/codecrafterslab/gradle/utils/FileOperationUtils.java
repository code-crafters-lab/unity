package org.codecrafterslab.gradle.utils;

import org.gradle.api.initialization.Settings;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.file.*;
import org.gradle.api.invocation.Gradle;
import org.gradle.internal.service.ServiceRegistry;

import java.io.File;

/**
 * @author WuYujie
 * @since 0.6.0
 */
public class FileOperationUtils {

    public static FileOperations fileOperationsFor(Settings settings) {
        return fileOperationsFor(settings.getGradle(), settings.getRootDir());
    }

    public static FileOperations fileOperationsFor(Gradle gradle, File baseDir) {
        GradleInternal gradleInternal = (GradleInternal) gradle;
        ServiceRegistry services = gradleInternal.getServices();
        return fileOperationsFor(services, baseDir);
    }

    static FileOperations fileOperationsFor(ServiceRegistry serviceRegistry, File baseDir) {
        FileLookup fileLookup = serviceRegistry.get(FileLookup.class);
        FileResolver fileResolver;
        if (baseDir != null) {
            fileResolver = fileLookup.getFileResolver(baseDir);
        } else {
            fileResolver = fileLookup.getFileResolver();
        }
        FileCollectionFactory fileCollectionFactory =
                serviceRegistry.get(FileCollectionFactory.class).withResolver(fileResolver);
        return DefaultFileOperations.createSimple(fileResolver, fileCollectionFactory, serviceRegistry);
    }
}
