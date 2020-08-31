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
    id("gradlebuild.publish-public-libraries")
}

description = "Package build cache results"

dependencies {
    api(project(":distribution-core:build-cache-base"))
    api(project(":distribution-core:snapshots"))
    api(project(":distribution-core:hashing"))
    api(project(":distribution-core:files"))

    implementation(project(":distribution-core:base-annotations"))

    implementation(libs.guava)
    implementation(libs.commonsCompress)
    implementation(libs.commonsIo)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:resources"))

    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:snapshots")))
    testImplementation(testFixtures(project(":distribution-core:core-api")))
}
