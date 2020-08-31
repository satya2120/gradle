plugins {
    id("gradlebuild.distribution.packaging")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-basics")))

    pluginsRuntimeOnly(project(":distribution-plugins:jvm:scala"))
    pluginsRuntimeOnly(project(":distribution-plugins:jvm:ear"))
    pluginsRuntimeOnly(project(":distribution-plugins:jvm:code-quality"))
    pluginsRuntimeOnly(project(":distribution-plugins:jvm:jacoco"))
    pluginsRuntimeOnly(project(":distribution-plugins:jvm:ide"))
}
