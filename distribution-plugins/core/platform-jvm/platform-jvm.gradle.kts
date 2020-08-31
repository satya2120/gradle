import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.jmh")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:diagnostics"))
    implementation(project(":distribution-core:normalization-java"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:native"))

    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsCompress)
    implementation(libs.commonsIo)
    implementation(libs.inject)
    implementation(libs.asm)
    implementation(libs.nativePlatform)

    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(libs.ant)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))
    testImplementation(testFixtures(project(":distribution-core:logging")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))
    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))

    integTestImplementation(libs.slf4jApi)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

strictCompile {
    ignoreDeprecations() // most of this project has been deprecated
}

integrationTestUsesSampleDir("subprojects/:distribution-plugins:core:platform-jvm/src/main")
