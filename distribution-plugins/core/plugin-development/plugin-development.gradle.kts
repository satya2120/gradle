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
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:maven"))
    implementation(project(":distribution-plugins:core:ivy"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:testing-base"))
    implementation(project(":distribution-plugins:core:testing-jvm"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:plugin-use"))
    implementation(project(":distribution-plugins:core:publish"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-core:model-groovy"))
    implementation(project(":distribution-core:resources"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.commonsIo)
    implementation(libs.guava)
    implementation(libs.inject)
    implementation(libs.asm)

    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:logging")))

    integTestImplementation(project(":distribution-core:base-services-groovy"))
    integTestImplementation(libs.jetbrainsAnnotations)

    integTestLocalRepository(project(":distribution-core:tooling-api")) {
        because("Required by GradleImplDepsCompatibilityIntegrationTest")
    }

    testRuntimeOnly(project(":distribution-setup:distributions-basics")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/plugin-development/src/main")
