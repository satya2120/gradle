plugins {
    id("gradlebuild.distribution.api-java")
}

description = "A set of general-purpose resource abstractions"

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsIo)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:core-api"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:snapshots"))

    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
