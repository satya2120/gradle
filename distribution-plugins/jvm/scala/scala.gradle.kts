/*
 * Copyright 2010 the original author or authors.
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
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:jvm:language-scala"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-core:process-services"))

    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(project(":distribution-core:files"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(libs.slf4jApi)
    testImplementation(libs.commonsIo)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:plugins")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-java")))

    integTestImplementation(project(":distribution-core:jvm-services"))
    integTestImplementation(testFixtures(project(":distribution-plugins:jvm:language-scala")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/api/internal/tasks/scala/**",
        // Unable to change package of public API
        "org/gradle/api/tasks/ScalaRuntime*"))
}

integrationTestUsesSampleDir("subprojects/scala/src/main")
