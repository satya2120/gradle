/*
 * Copyright 2011 the original author or authors.
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
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:diagnostics"))

    implementation(libs.nativePlatform)
    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.snakeyaml)
    implementation(libs.gson)
    implementation(libs.inject)

    testFixturesApi(project(":distribution-core:resources"))
    testFixturesApi(testFixtures(project(":distribution-plugins:jvm:ide")))
    testFixturesImplementation(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(project(":distribution-core:native"))
    testFixturesImplementation(project(":distribution-plugins:core:platform-base"))
    testFixturesImplementation(project(":distribution-core:file-collections"))
    testFixturesImplementation(project(":distribution-core:process-services"))
    testFixturesImplementation(project(":distribution-core:snapshots"))
    testFixturesImplementation(libs.guava)
    testFixturesImplementation(libs.nativePlatform)
    testFixturesImplementation(libs.commonsLang)
    testFixturesImplementation(libs.commonsIo)

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))
    testImplementation(testFixtures(project(":distribution-core:model-core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-core:snapshots")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-native")) {
        because("Required 'ideNative' to test visual studio project file generation for generated sources")
    }
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/nativeplatform/plugins/**",
        "org/gradle/nativeplatform/tasks/**",
        "org/gradle/nativeplatform/internal/resolve/**",
        "org/gradle/nativeplatform/toolchain/internal/**"
    ))
}

integrationTestUsesSampleDir("subprojects/platform-native/src/main")
