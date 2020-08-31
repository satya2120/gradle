/*
 * Copyright 2017 the original author or authors.
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
    id("gradlebuild.jmh")
}

dependencies {
    api(project(":distribution-core:build-cache-base"))
    api(project(":distribution-core:snapshots"))

    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:logging"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.inject)

    jmhImplementation(platform(project(":distribution-setup:distributions-dependencies")))
    jmhImplementation(libs.ant)
    jmhImplementation(libs.commonsCompress)
    jmhImplementation(libs.aircompressor)
    jmhImplementation(libs.snappy)
    jmhImplementation(libs.jtar)

    testImplementation(project(":distribution-core:model-core"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
