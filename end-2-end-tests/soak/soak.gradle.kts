/*
 * Copyright 2019 the original author or authors.
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
    id("gradlebuild.internal.kotlin")
}

dependencies {
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))

    testImplementation(testFixtures(project(":distribution-core:kotlin-dsl")))
    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(project(":distribution-core:persistent-cache"))
    integTestImplementation(project(":distribution-core:launcher"))
    integTestImplementation(project(":distribution-core:file-watching"))
    integTestImplementation(libs.slf4jApi)
    integTestImplementation(libs.jetty)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}

tasks.register("soakTest") {
    description = "Run all soak tests defined in the :soak subproject"
    group = "CI Lifecycle"
    dependsOn(":soak:embeddedIntegTest")
}
