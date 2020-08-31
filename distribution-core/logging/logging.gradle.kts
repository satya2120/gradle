plugins {
    id("gradlebuild.distribution.api-java")
}

description = "Logging infrastructure"

gradlebuildJava.usedInWorkers()

dependencies {
    api(libs.slf4jApi)

    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:cli"))
    implementation(project(":distribution-core:build-option"))

    implementation(project(":distribution-core:native"))
    implementation(libs.julToSlf4j)
    implementation(libs.ant)
    implementation(libs.commonsLang)
    implementation(libs.guava)
    implementation(libs.jansi)

    runtimeOnly(libs.log4jToSlf4j)
    runtimeOnly(libs.jclToSlf4j)

    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestImplementation(libs.ansiControlSequenceUtil)

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(libs.slf4jApi)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/internal/featurelifecycle/**"))
}
