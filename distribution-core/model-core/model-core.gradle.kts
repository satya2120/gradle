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
    api(project(":distribution-core:core-api"))

    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:snapshots"))

    implementation(libs.futureKotlin("stdlib"))
    implementation(libs.inject)
    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.asm)

    testFixturesApi(testFixtures(project(":distribution-plugins:core:diagnostics")))
    testFixturesApi(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(libs.guava)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(testFixtures(project(":distribution-core:core-api")))

    integTestImplementation(project(":distribution-plugins:core:platform-base"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/model/internal/core/**",
        "org/gradle/model/internal/inspect/**",
        "org/gradle/api/internal/tasks/**",
        "org/gradle/model/internal/manage/schema/**",
        "org/gradle/model/internal/type/**",
        "org/gradle/api/internal/plugins/*"
    ))
}
