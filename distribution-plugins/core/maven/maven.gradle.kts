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
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:plugin-use"))
    implementation(project(":distribution-plugins:core:publish"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.inject)
    implementation(libs.ant)
    implementation(libs.ivy)
    implementation(libs.maven3)
    implementation(libs.pmavenCommon)
    implementation(libs.pmavenGroovy)
    implementation(libs.maven3WagonFile)
    implementation(libs.maven3WagonHttp)
    implementation(libs.plexusContainer)
    implementation(libs.aetherConnector)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(project(":distribution-plugins:basics:resources-http"))
    testImplementation(libs.xmlunit)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:model-core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))

    integTestImplementation(project(":distribution-plugins:jvm:ear"))
    integTestImplementation(libs.jetty)

    testFixturesApi(project(":distribution-core:base-services")) {
        because("Test fixtures export the Action class")
    }
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(project(":distribution-plugins:core:dependency-management"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

strictCompile {
    ignoreDeprecations() // old 'maven' publishing mechanism: types are deprecated
    ignoreRawTypes() // old 'maven' publishing mechanism: raw types used in public API
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/api/publication/maven/internal/**",
        "org/gradle/api/artifacts/maven/**"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/maven/src/main")
