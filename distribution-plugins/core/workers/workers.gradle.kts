plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:resources"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:logging")))

    integTestRuntimeOnly(project(":distribution-core:kotlin-dsl"))
    integTestRuntimeOnly(project(":distribution-plugins:core:kotlin-dsl-provider-plugins"))
    integTestRuntimeOnly(project(":distribution-core:api-metadata"))
    integTestRuntimeOnly(project(":distribution-plugins:basics:test-kit"))

    integTestImplementation(project(":distribution-core:jvm-services"))

    testFixturesImplementation(libs.inject)
    testFixturesImplementation(project(":distribution-core:base-services"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
