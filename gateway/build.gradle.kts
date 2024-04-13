plugins {
    java
}

dependencies {
    implementation(platform(project(":unity-dependencies")))
    
    implementation("io.envoyproxy.controlplane:api")
}