package org.codecrafterslab.gradle.ide

import java.util.*

enum class IDE(val value: String) {
    /**
     * 开发工具
     */
    NONE("none"),
    IDEA("idea"),
    ECLIPSE("eclipse"),
    VISUAL_STUDIO("visual_studio"),
    XCODE("xcode");


    companion object {
        fun of(ideName: String?): IDE {
            return Arrays.stream(IDE.values())
                .filter { it.value.equals(ideName, ignoreCase = true) }
                .findFirst().orElse(NONE)
        }
    }
}