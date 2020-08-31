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
    implementation(project(":distribution-core:base-services-groovy")) // for 'Specs'
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:core:publish"))
    implementation(project(":distribution-plugins:core:plugins")) // for base plugin to get archives conf
    implementation(project(":distribution-plugins:core:plugin-use"))
    implementation(project(":distribution-plugins:core:dependency-management"))

    implementation(libs.groovy) // for 'Closure' and 'Task.property(String propertyName) throws groovy.lang.MissingPropertyException'
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.inject)
    implementation(libs.ivy)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:snapshots"))

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:model-core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))

    integTestImplementation(project(":distribution-plugins:jvm:ear"))
    integTestImplementation(libs.slf4jApi)
    integTestImplementation(libs.jetty)

    integTestRuntimeOnly(project(":distribution-plugins:basics:resources-s3"))
    integTestRuntimeOnly(project(":distribution-plugins:basics:resources-sftp"))
    integTestRuntimeOnly(project(":distribution-core:api-metadata"))

    testFixturesApi(project(":distribution-core:base-services")) {
        because("Test fixtures export the Action class")
    }
    testFixturesApi(project(":distribution-core:core-api")) {
        because("Test fixtures export the RepositoryHandler class")
    }
    testFixturesImplementation(project(":distribution-core:logging"))
    testFixturesImplementation(project(":distribution-plugins:core:dependency-management"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(libs.slf4jApi)
    testFixturesImplementation(libs.sshdCore)
    testFixturesImplementation(libs.sshdScp)
    testFixturesImplementation(libs.sshdSftp)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}


integrationTestUsesSampleDir("subprojects/ivy/src/main")
