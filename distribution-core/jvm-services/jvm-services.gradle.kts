plugins {
    id("gradlebuild.distribution.api-java")
}

description = "JVM invocation and inspection abstractions"

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:process-services"))

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:core-api"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(libs.slf4jApi)
    testImplementation(testFixtures(project(":distribution-core:core")))
}
