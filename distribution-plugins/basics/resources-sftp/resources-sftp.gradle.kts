/*
 * Copyright 2015 the original author or authors.
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

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:core"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.jsch)
    implementation(libs.commonsIo)

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:core:ivy")))
    testImplementation(testFixtures(project(":distribution-plugins:core:maven")))

    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(libs.jetty)
    integTestImplementation(libs.sshdCore)
    integTestImplementation(libs.sshdScp)
    integTestImplementation(libs.sshdSftp)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
