package org.codecrafterslab.gradle.plugins.openapi.tasks

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.JavaExec

@CacheableTask
open class GenerateTask : JavaExec() {
}
