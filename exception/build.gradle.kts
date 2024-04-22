plugins {
    id("com.voc.bom")
}

description = "Unity Exception"
group = "org.codecrafterslab.unity"

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "com.voc.publish")
    apply(plugin = "net.jqsoft.nexus3")
}
