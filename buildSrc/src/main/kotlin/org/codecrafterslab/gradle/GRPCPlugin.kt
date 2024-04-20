package org.codecrafterslab.gradle

import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.ProtobufPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class GRPCPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(ProtobufPlugin::class.java)) {
            project.plugins.apply(ProtobufPlugin::class.java)
        }

        val protocVersion = project.properties.getOrDefault("protoc.version", "4.26.0")
        val grpcVersion = project.properties.getOrDefault("grpc.version", "1.62.2")

        val protocArtifact = "com.google.protobuf:protoc:${protocVersion}"
        val grpcArtifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"

        if (project.logger.isDebugEnabled) {
            project.logger.debug("protoc\t=>\t{}", protocArtifact)
            project.logger.debug("grpc\t=>\t{}", grpcArtifact)
        }

        project.extensions.getByType(ProtobufExtension::class.java).apply {
            protoc {
                artifact = protocArtifact
            }

            plugins {
                create("grpc") {
                    artifact = grpcArtifact
                }
            }
            generateProtoTasks {
                all().configureEach {
                    plugins {
                        create("grpc") {}
                    }
                }
            }
        }
    }
}