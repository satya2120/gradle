import gradlebuild.cleanup.WhenNotEmpty

/*
 * Copyright 2014 the original author or authors.
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
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-core:build-option"))

    implementation(libs.groovy)
    implementation(libs.guava)

    testImplementation(testFixtures(project(":distribution-plugins:basics:resources-http")))

    integTestImplementation(project(":distribution-core:base-services-groovy"))
    integTestImplementation(libs.jetbrainsAnnotations)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics")) {
        because("Requires test-kit: 'java-gradle-plugin' is used in integration tests which always adds the test-kit dependency.")
    }
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

