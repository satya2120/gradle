import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-core:build-events"))
    implementation(project(":distribution-core:tooling-api"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.fastutil)
    implementation(libs.ant) // for 'ZipFile' and 'ZipEntry'
    implementation(libs.asm)
    implementation(libs.asmCommons)
    implementation(libs.inject)

    runtimeOnly(project(":distribution-plugins:core:java-compiler-plugin"))

    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(libs.commonsIo)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))

    testFixturesApi(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:model-core"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(project(":distribution-plugins:core:platform-base"))
    testFixturesImplementation(project(":distribution-core:persistent-cache"))
    testFixturesImplementation(libs.slf4jApi)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder test (JavaLanguagePluginTest) loads services from a Gradle distribution.")
    }

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))

    buildJvms.whenTestingWithEarlierThan(JavaVersion.VERSION_1_9) {
        val tools = it.jdk.get().toolsClasspath
        testRuntimeOnly(tools)
    }
}

strictCompile {
    ignoreDeprecations() // this project currently uses many deprecated part from ':distribution-plugins:core:platform-jvm'
}

classycle {
    // These public packages have classes that are tangled with the corresponding internal package.
    excludePatterns.set(listOf(
        "org/gradle/api/tasks/compile/**",
        "org/gradle/external/javadoc/**"
    ))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/language-java/src/main")
