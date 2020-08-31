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
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:native:platform-native"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:publish"))
    implementation(project(":distribution-plugins:core:maven"))
    implementation(project(":distribution-plugins:core:ivy"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-plugins:core:version-control"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.inject)

    testFixturesApi(project(":distribution-core:base-services")) {
        because("Test fixtures export the Named class")
    }
    testFixturesApi(project(":distribution-plugins:core:platform-base")) {
        because("Test fixtures export the Platform class")
    }

    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))

    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:version-control")))
    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))
    testImplementation(testFixtures(project(":distribution-plugins:core:platform-base")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:snapshots")))

    integTestImplementation(project(":distribution-core:native"))
    integTestImplementation(project(":distribution-core:resources"))
    integTestImplementation(libs.nativePlatform)
    integTestImplementation(libs.ant)
    integTestImplementation(libs.jgit)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-native"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/language/nativeplatform/internal/**"))
}

integrationTestUsesSampleDir("subprojects/language-native/src/main")
