import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.integrationTestUsesSampleDir

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
plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:core:language-groovy"))
    implementation(project(":distribution-plugins:core:diagnostics"))
    implementation(project(":distribution-plugins:core:testing-base"))
    implementation(project(":distribution-plugins:core:testing-jvm"))
    implementation(project(":distribution-core:snapshots"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.ant)
    implementation(libs.asm)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.inject)

    testImplementation(project(":distribution-core:messaging"))
    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(libs.gson) {
        because("for unknown reason (bug in the Groovy/Spock compiler?) requires it to be present to use the Gradle Module Metadata test fixtures")
    }
    testImplementation(libs.jsoup)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:basics:resources-http")))
    testImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-jvm")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-java")))
    testImplementation(testFixtures(project(":distribution-plugins:core:language-groovy")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))

    testFixturesImplementation(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(project(":distribution-core:base-services-groovy"))
    testFixturesImplementation(project(":distribution-core:file-collections"))
    testFixturesImplementation(project(":distribution-plugins:core:language-jvm"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(project(":distribution-core:process-services"))
    testFixturesImplementation(project(":distribution-core:resources"))
    testFixturesImplementation(libs.guava)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
    ignoreDeprecations() // uses deprecated software model types
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/plugins/src/main")
