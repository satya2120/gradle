import gradlebuild.cleanup.WhenNotEmpty

plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.launchable-jar")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:cli"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:build-option"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:bootstrap"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-core:build-events"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-core:file-watching"))

    implementation(libs.groovy) // for 'ReleaseInfo.getVersion()'
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.asm)
    implementation(libs.ant)

    runtimeOnly(libs.asm)
    runtimeOnly(libs.commonsIo)
    runtimeOnly(libs.commonsLang)
    runtimeOnly(libs.slf4jApi)

    manifestClasspath(project(":distribution-core:bootstrap"))
    manifestClasspath(project(":distribution-core:base-services"))
    manifestClasspath(project(":distribution-core:core-api"))
    manifestClasspath(project(":distribution-core:core"))
    manifestClasspath(project(":distribution-core:persistent-cache"))

    testImplementation(project(":fixtures:internal-integ-testing"))
    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:cli"))
    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:core-api"))
    testImplementation(project(":distribution-core:model-core"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(project(":distribution-core:base-services-groovy")) // for 'Specs'

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-java")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:logging")))
    testImplementation(testFixtures(project(":distribution-core:tooling-api")))

    integTestImplementation(project(":distribution-core:persistent-cache"))
    integTestImplementation(libs.slf4jApi)
    integTestImplementation(libs.guava)
    integTestImplementation(libs.commonsLang)
    integTestImplementation(libs.commonsIo)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-native")) {
        because("':distribution-core:native' distribution requried for 'ProcessCrashHandlingIntegrationTest.session id of daemon is different from daemon client'")
    }
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

// Needed for testing debug command line option (JDWPUtil) - 'CommandLineIntegrationSpec.can debug with org.gradle.debug=true'
val toolsJar = buildJvms.testJvm.map { jvm -> jvm.jdk.get().toolsClasspath }
dependencies {
    integTestRuntimeOnly(toolsJar)
}
