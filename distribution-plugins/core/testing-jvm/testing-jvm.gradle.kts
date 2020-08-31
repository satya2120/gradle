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
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
}

gradlebuildJava.usedInWorkers()

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:diagnostics"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:core:testing-base"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.asm)
    implementation(libs.junit)
    implementation(libs.testng)
    implementation(libs.inject)
    implementation(libs.bsh)

    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(libs.guice) {
        because("This is for TestNG")
    }
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:testing-base")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
}

strictCompile {
    ignoreRawTypes() // raw types used in public API (org.gradle.api.tasks.testing.Test)
    ignoreDeprecations() // uses deprecated software model types
}

classycle {
    excludePatterns.set(listOf("org/gradle/api/internal/tasks/testing/**"))
}

tasks.named<Test>("test").configure {
    exclude("org/gradle/api/internal/tasks/testing/junit/AJunit*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/BJunit*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/ATestClass*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/ATestSetUp*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/ABroken*TestClass*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/ATestSetUpWithBrokenSetUp*.*")
    exclude("org/gradle/api/internal/tasks/testing/testng/ATestNGFactoryClass*.*")
}

integrationTestUsesSampleDir("subprojects/testing-jvm/src/main")
