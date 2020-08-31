/*
 * Copyright 2012 the original author or authors.
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
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:core:dependency-management"))

    implementation(libs.guava)
    implementation(libs.inject)
    implementation(libs.jgit)
    implementation(libs.commonsHttpclient)
    implementation(libs.jsch)

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:snapshots"))
    testImplementation(project(":distribution-core:process-services"))
    testImplementation(testFixtures(project(":distribution-core:core")))

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))

    testFixturesImplementation(libs.jgit)
    testFixturesImplementation(libs.commonsIo)
    testFixturesImplementation(libs.commonsHttpclient)
    testFixturesImplementation(libs.jsch)
    testFixturesImplementation(libs.guava)

    integTestImplementation(project(":distribution-core:launcher"))
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}
