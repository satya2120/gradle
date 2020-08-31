import gradlebuild.basics.BuildEnvironment
import gradlebuild.integrationtests.integrationTestUsesSampleDir
import gradlebuild.integrationtests.tasks.IntegrationTest

plugins {
    id("gradlebuild.distribution.api-java")
}

val integTestRuntimeResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = false
}
val integTestRuntimeResourcesClasspath: Configuration by configurations.creating {
    extendsFrom(integTestRuntimeResources)
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        // play test apps MUST be found as exploded directory
        attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements::class.java, LibraryElements.RESOURCES))
    }
    isTransitive = false
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:jvm:language-scala"))
    implementation(project(":distribution-plugins:core:testing-base"))
    implementation(project(":distribution-plugins:core:testing-jvm"))
    implementation(project(":distribution-plugins:full:javascript"))
    implementation(project(":distribution-plugins:core:diagnostics"))
    implementation(project(":distribution-plugins:core:reporting"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:base-services-groovy"))

    integTestImplementation(libs.ant)
    integTestRuntimeOnly(project(":distribution-plugins:core:composite-builds"))
    integTestRuntimeOnly(project(":distribution-plugins:full:ide-play"))
    integTestRuntimeOnly(project(":distribution-plugins:core:testing-junit-platform"))

    testFixturesApi(project(":distribution-plugins:core:platform-base")) {
        because("Test fixtures export the Platform class")
    }
    testFixturesApi(testFixtures(project(":distribution-core:core")))
    testFixturesApi(testFixtures(project(":distribution-plugins:native:platform-native")))
    testFixturesApi(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testFixturesApi(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(project(":distribution-core:process-services"))
    testFixturesImplementation(libs.commonsIo)
    testFixturesImplementation(libs.commonsHttpclient)
    testFixturesImplementation(libs.slf4jApi)
    testFixturesApi(testFixtures(project(":distribution-plugins:jvm:language-scala")))
    testFixturesApi(testFixtures(project(":distribution-plugins:core:language-java")))

    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestRuntimeResources(testFixtures(project(":distribution-plugins:full:platform-play")))
}

strictCompile {
    ignoreRawTypes() // deprecated raw types
    ignoreDeprecations() // uses deprecated software model types
}
val integTestPrepare by tasks.registering(IntegrationTest::class) {
    systemProperties["org.gradle.integtest.executer"] = "embedded"
    if (BuildEnvironment.isCiServer) {
        systemProperties["org.gradle.integtest.multiversion"] = "all"
    }
    include("org/gradle/play/prepare/**")
    maxParallelForks = 1
}

tasks.withType<IntegrationTest>().configureEach {
    if (name != "integTestPrepare") {
        dependsOn(integTestPrepare)
        exclude("org/gradle/play/prepare/**")
        // this is a workaround for which we need a better fix:
        // it sets the platform play test fixtures :distribution-core:resources directory in front
        // of the classpath, so that we can find them when executing tests in
        // an exploded format, rather than finding them in the test fixtures jar
        classpath = integTestRuntimeResourcesClasspath + classpath
    }
}

integrationTestUsesSampleDir("subprojects/platform-play/src/main")
