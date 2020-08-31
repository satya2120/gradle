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
plugins {
    id("gradlebuild.distribution.api-java")
}

description = "Execution engine that takes a unit of work and makes it happen"

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:build-cache-packaging"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:logging"))
    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:model-core"))
    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-core:file-collections")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:snapshots")))
    testImplementation(testFixtures(project(":distribution-core:core")))

    testFixturesImplementation(libs.guava)
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:build-cache"))
    testFixturesImplementation(project(":distribution-core:snapshots"))
    testFixturesImplementation(project(":distribution-core:model-core"))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
