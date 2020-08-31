plugins {
    id("gradlebuild.distribution.packaging")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-basics")))

    pluginsRuntimeOnly(project(":distribution-plugins:publishing:signing"))
}
