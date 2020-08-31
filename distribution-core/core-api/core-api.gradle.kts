import gradlebuild.cleanup.WhenNotEmpty

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:resources"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.ant)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.inject)

    testImplementation(libs.asm)
    testImplementation(libs.asmCommons)
    testImplementation(testFixtures(project(":distribution-core:logging")))

    testFixturesImplementation(project(":distribution-core:base-services"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
    ignoreParameterizedVarargType() // [unchecked] Possible heap pollution from parameterized vararg type: ArtifactResolutionQuery, RepositoryContentDescriptor, HasMultipleValues
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
