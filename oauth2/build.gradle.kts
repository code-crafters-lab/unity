plugins {
    id("ccl.bom")
}

description = "Unity OAuth"
group = "org.codecrafterslab.unity"

subprojects {
    group = "org.codecrafterslab.unity"

//    apply(plugin = "ccl.lib")
    //    apply(plugin = "net.jqsoft.nexus3")

//    dependencies {
//        management(platform("org.codecrafterslab.unity:dependencies"))
//    }
    
}

listOf("build", "clean", "publish", "publishToMavenLocal").forEach { task ->
    tasks.named(task) {
        dependsOn(
            ":oauth2-authorization-server:${task}",
//            ":oauth2-resource-server:${task}",
            ":oauth2-security:${task}",
        )
    }
}