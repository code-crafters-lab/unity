package org.codecrafterslab.build.settings.conventions

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.version

class InnerConventions : Plugin<Settings> {
    private val innerPluginList = listOf(
        "com.voc.bom", "com.voc.lib", "com.voc.app", "com.voc.publish",
        "com.voc.repo", "com.voc.settings",
        "net.jqsoft.nexus3",
    )

    @Suppress("unused")
    private val logger: Logger = Logging.getLogger(Settings::class.java);

    override fun apply(settings: Settings) {

        settings.gradle.settingsEvaluated {
            pluginManagement {
                plugins {
                    innerPluginList.forEach {
                        // 如果插件已经在类路径上，则插件声明不能使用版本
                        id(it) apply false
                        id("com.google.protobuf") version "0.9.4" apply false
                    }
                }

                resolutionStrategy {
                    eachPlugin {
                        innerPluginList.forEach {
                            if (requested.id.id.startsWith(it)) {
                                useVersion(
                                    extra.properties.getOrDefault("inner.plugins.version", "0.9.0-beta.10")
                                        .toString()
                                )
                                if (logger.isDebugEnabled) {
                                    logger.debug("plugin {} use version {}", target.id, target.version)
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}