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
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:native:platform-native"))
    implementation(project(":distribution-plugins:native:language-native"))
    implementation(project(":distribution-plugins:native:testing-native"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-plugins:jvm:ide")) {
        because("To pick up various builders (which should live somewhere else)")
    }

    implementation(libs.guava)

    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))

    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-native"))
}
