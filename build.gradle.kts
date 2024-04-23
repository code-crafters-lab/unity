plugins {
    id("com.voc.publish")
}

group = "org.codecrafterslab"
description = "Unity Build"

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "com.voc.publish")
    apply(plugin = "net.jqsoft.nexus3")
}

configurations.all {
    
}



