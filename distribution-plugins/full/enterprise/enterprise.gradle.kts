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

dependencies {
    api(project(":distribution-core:base-services")) // leaks BuildOperationNotificationListener on API

    implementation(libs.jsr305)
    implementation(libs.inject)
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:launcher"))
    implementation(project(":distribution-core:snapshots"))

    integTestImplementation(project(":fixtures:internal-testing"))
    integTestImplementation(project(":fixtures:internal-integ-testing"))

    // Dependencies of the integ test fixtures
    integTestImplementation(project(":distribution-core:build-option"))
    integTestImplementation(project(":distribution-core:messaging"))
    integTestImplementation(project(":distribution-core:persistent-cache"))
    integTestImplementation(project(":distribution-core:native"))
    integTestImplementation(libs.guava)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}
