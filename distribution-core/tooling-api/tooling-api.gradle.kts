/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.publish-public-libraries")
    id("gradlebuild.shaded-jar")
}

shadedJar {
    shadedConfiguration.exclude(mapOf("group" to "org.slf4j", "module" to "slf4j-api"))
    keepPackages.set(listOf("org.gradle.tooling"))
    unshadedPackages.set(listOf("org.gradle", "org.slf4j", "sun.misc"))
    ignoredPackages.set(setOf("org.gradle.tooling.provider.model"))
}

dependencies {
    shadedImplementation(libs.slf4jApi)

    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:wrapper"))
    implementation(project(":distribution-core:persistent-cache"))

    implementation(libs.guava)

    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(project(":distribution-core:model-core"))
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:base-services-groovy"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(libs.commonsIo)
    testFixturesImplementation(libs.slf4jApi)

    integTestImplementation(project(":distribution-core:jvm-services"))
    integTestImplementation(project(":distribution-core:persistent-cache"))

    crossVersionTestImplementation(project(":distribution-core:jvm-services"))
    crossVersionTestImplementation(libs.jetty)
    crossVersionTestImplementation(libs.commonsIo)
    crossVersionTestRuntimeOnly(libs.cglib) {
        because("BuildFinishedCrossVersionSpec classpath inference requires cglib enhancer")
    }

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:logging")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:jvm:ide")))
    testImplementation(testFixtures(project(":distribution-plugins:core:workers")))

    integTestNormalizedDistribution(project(":distribution-setup:distributions-full")) {
        because("Used by ToolingApiRemoteIntegrationTest")
    }

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
    integTestLocalRepository(project(path)) {
        because("ToolingApiResolveIntegrationTest and ToolingApiClasspathIntegrationTest use the Tooling API Jar")
    }

    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
    crossVersionTestLocalRepository(project(path)) {
        because("ToolingApiVersionSpecification uses the Tooling API Jar")
    }
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
}

classycle {
    excludePatterns.set(listOf("org/gradle/tooling/**"))
}

apply(from = "buildship.gradle")

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
integrationTestUsesSampleDir("subprojects/tooling-api/src/main")
