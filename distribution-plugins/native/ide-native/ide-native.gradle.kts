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
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:jvm:ide"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:native:platform-native"))
    implementation(project(":distribution-plugins:native:language-native"))
    implementation(project(":distribution-plugins:core:testing-base"))
    implementation(project(":distribution-plugins:native:testing-native"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.inject)
    implementation(libs.plist)

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))
    testImplementation(testFixtures(project(":distribution-plugins:native:language-native")))
    testImplementation(testFixtures(project(":distribution-plugins:core:version-control")))

    integTestImplementation(project(":distribution-core:native"))
    integTestImplementation(libs.commonsIo)
    integTestImplementation(libs.jgit)

    testFixturesApi(testFixtures(project(":distribution-plugins:jvm:ide")))
    testFixturesImplementation(libs.plist)
    testFixturesImplementation(libs.guava)
    testFixturesImplementation(testFixtures(project(":distribution-plugins:jvm:ide")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-native"))
}

integrationTestUsesSampleDir("subprojects/ide-native/src/main")
