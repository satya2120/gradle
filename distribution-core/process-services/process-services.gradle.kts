/**
 * Process execution abstractions.
 */
plugins {
    id("gradlebuild.distribution.api-java")
}

gradlebuildJava.usedInWorkers()

dependencies {
    implementation(project(":distribution-core:base-services"))

    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.nativePlatform)

    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/process/internal/**"))
}
