plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-core:files"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.asm)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(project(":fixtures:internal-testing"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(testFixtures(project(":distribution-core:core")))

    testFixturesApi(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(project(":distribution-core:base-services"))

    integTestImplementation(libs.commonsLang)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/api/internal/tasks/compile/**",
        "org/gradle/api/tasks/javadoc/**"
    ))
}
