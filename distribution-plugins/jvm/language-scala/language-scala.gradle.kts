plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:core:language-jvm"))

    implementation(libs.groovy) // for 'Task.property(String propertyName) throws groovy.lang.MissingPropertyException'
    implementation(libs.ant)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:files"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))
    testImplementation(testFixtures(project(":distribution-plugins:core:plugins")))

    integTestImplementation(libs.commonsLang)
    integTestImplementation(libs.ant)

    testFixturesApi(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:model-core"))
    testFixturesImplementation(project(":distribution-plugins:core:platform-base"))
    testFixturesImplementation(testFixtures(project(":distribution-plugins:core:language-jvm")))

    compileOnly("org.scala-sbt:zinc_2.12:1.3.5")

    testRuntimeOnly(project(":distribution-setup:distributions-jvm")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
}

strictCompile {
    ignoreDeprecations() // uses deprecated software model types
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/api/internal/tasks/scala/**",
        "org/gradle/language/scala/internal/toolchain/**"))
}

