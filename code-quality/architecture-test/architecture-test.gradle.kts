import gradlebuild.basics.PublicApi

plugins {
    id("gradlebuild.internal.java")
    id("gradlebuild.binary-compatibility")
}

dependencies {
    currentClasspath(project(":distribution-setup:distributions-full"))
    testImplementation(project(":distribution-core:base-services"))
    testImplementation(project(":distribution-core:model-core"))

    testImplementation(libs.archunitJunit4)
    testImplementation(libs.guava)

    testRuntimeOnly(project(":distribution-setup:distributions-full"))
}

tasks.test {
    // Looks like loading all the classes requires more than the default 512M
    maxHeapSize = "700M"

    systemProperty("org.gradle.public.api.includes", PublicApi.includes.joinToString(":"))
    systemProperty("org.gradle.public.api.excludes", PublicApi.excludes.joinToString(":"))
}
