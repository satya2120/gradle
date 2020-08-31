import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-core:execution"))

    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.commonsLang)

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:core-api")))
    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(project(":distribution-core:process-services"))

    testFixturesApi(project(":distribution-core:core"))
    testFixturesApi(project(":distribution-core:file-collections"))
    testFixturesApi(testFixtures(project(":distribution-core:model-core")))
    testFixturesImplementation(libs.guava)
    testFixturesApi(testFixtures(project(":distribution-core:model-core")))
    testFixturesApi(testFixtures(project(":distribution-plugins:core:diagnostics")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("RuntimeShadedJarCreatorTest requires a distribution to access the ...-relocated.txt metadata")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

integrationTestUsesSampleDir("subprojects/platform-base/src/main")
