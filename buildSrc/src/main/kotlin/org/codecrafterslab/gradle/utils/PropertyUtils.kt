package org.codecrafterslab.gradle.utils

import org.gradle.api.Project
import org.springframework.util.StringUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*


class PropertyUtils {

    companion object {
        private var cacheMaps = mutableMapOf<Project, MutableList<Properties>>()

        /**
         * @param key   属性名称
         * @param project 项目信息
         * @return 属性值
         */
        fun getPriorityProperty(key: String, project: Project, defaultValue: String?): String? {
            redInit(project)
            val properties = readGradleProperties(project)
            val localProperties = readGradleLocalProperties(project)
            return getPriorityProperty(key, defaultValue, localProperties, properties)
        }

        fun getPriorityProperty(key: String, defaultValue: String?, vararg properties: Properties): String? {
            val sys = System.getProperty(key)
            if (StringUtils.hasText(sys)) return sys
            val env = System.getenv(key.replace(".", "_").uppercase(Locale.getDefault()))
            if (StringUtils.hasText(env)) return env
            for (property in properties) {
                val value = property.getProperty(key)
                if (StringUtils.hasText(value)) return value
            }
            return defaultValue
        }

        /**
         * 读取属性文件
         *
         * @param file 文件名称
         * @return Properties
         */
        private fun readProperties(file: File): Properties {
            val localProperties = Properties()
            try {
                FileInputStream(file).use { input ->
                    localProperties.load(input)
                }
            } catch (ignored: IOException) {
            }
            return localProperties
        }

        private fun redInit(project: Project) {
            if (!cacheMaps.containsKey(project)) {
                cacheMaps[project] = mutableListOf(Properties(), Properties())
            }
        }

        private fun readGradleLocalProperties(project: Project): Properties {
            if (cacheMaps.containsKey(project) && cacheMaps[project]!![1].size == 0) {
                // todo 从父项目目录到当前项目目录，循环读取覆盖, 暂时从根项目读取
                val readProperties = readProperties(File(project.rootProject.projectDir, "gradle.local.properties"))
                cacheMaps[project]!![1] = readProperties
                return readProperties
            }
            return cacheMaps[project]!![1]
        }

        private fun readGradleProperties(project: Project): Properties {
            if (cacheMaps.containsKey(project) && cacheMaps[project]!![0].size == 0) {
                val gradleProperties = Properties()
                project.properties.keys.forEach {
                    if (project.properties[it] is String) {
                        gradleProperties[it] = project.properties[it]
                    }
                }
                cacheMaps[project]!![0] = gradleProperties
                return gradleProperties
            }
            return cacheMaps[project]!![0]
        }

    }
}
