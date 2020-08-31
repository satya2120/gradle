/*
 * Copyright 2020 the original author or authors.
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

plugins {
    id("gradlebuild.distribution.api-java")
}

description = "File system watchers for keeping the VFS up-to-date"

dependencies {
    api(project(":distribution-core:snapshots"))

    implementation(project(":distribution-core:base-annotations"))
    implementation(project(":distribution-core:build-operations"))

    implementation(libs.guava)
    implementation(libs.nativePlatform)
    implementation(libs.nativePlatformFileEvents)
    implementation(libs.slf4jApi)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:persistent-cache"))
    testImplementation(project(":distribution-core:build-option"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:file-collections")))
    testImplementation(testFixtures(project(":distribution-core:tooling-api")))
    testImplementation(testFixtures(project(":distribution-core:launcher")))

    testImplementation(libs.commonsIo)

    integTestImplementation(libs.jetty)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
