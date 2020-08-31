plugins {
    id("gradlebuild.internal.java")
    id("gradlebuild.performance-test")
    id("gradlebuild.performance-templates")
}

dependencies {
    performanceTestImplementation(project(":distribution-core:base-services"))
    performanceTestImplementation(project(":distribution-core:core"))
    performanceTestImplementation(project(":distribution-core:model-core"))
    performanceTestImplementation(project(":distribution-core:core-api"))
    performanceTestImplementation(project(":distribution-core:build-option"))
    performanceTestImplementation(libs.slf4jApi)
    performanceTestImplementation(libs.commonsIo)
    performanceTestImplementation(libs.commonsCompress)
    performanceTestImplementation(libs.jetty)
    performanceTestImplementation(testFixtures(project(":distribution-core:tooling-api")))

    performanceTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full")) {
        because("All Gradle features have to be available.")
    }
    performanceTestLocalRepository(project(":distribution-core:tooling-api")) {
        because("IDE tests use the Tooling API.")
    }
}
