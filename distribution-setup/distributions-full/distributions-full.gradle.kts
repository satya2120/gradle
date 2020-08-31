import gradlebuild.run.tasks.RunEmbeddedGradle

plugins {
    id("gradlebuild.distribution.packaging")
    id("gradlebuild.verify-build-environment")
    id("gradlebuild.install")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-publishing")))
    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-jvm")))
    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-native")))

    pluginsRuntimeOnly(project(":distribution-plugins:core:plugin-development"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:build-init"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:build-profile"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:antlr"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:enterprise"))

    // The following are scheduled to be removed from the distribution completely in Gradle 7.0
    pluginsRuntimeOnly(project(":distribution-plugins:full:javascript"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:platform-play"))
    pluginsRuntimeOnly(project(":distribution-plugins:full:ide-play"))
}

tasks.register<RunEmbeddedGradle>("runDevGradle") {
    group = "verification"
    description = "Runs an embedded Gradle using the partial distribution for ${project.path}."
    gradleClasspath.from(configurations.runtimeClasspath.get(), tasks.runtimeApiInfoJar)
}

// This is required for the separate promotion build and should be adjusted there in the future
tasks.register<Copy>("copyDistributionsToRootBuild") {
    dependsOn("buildDists")
    from(layout.buildDirectory.dir("distributions"))
    into(rootProject.layout.buildDirectory.dir("distributions"))
}
