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
import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:core:language-jvm"))
    implementation(project(":distribution-plugins:core:language-java"))
    implementation(project(":distribution-plugins:jvm:language-scala"))
    implementation(project(":distribution-plugins:jvm:scala"))
    implementation(project(":distribution-plugins:jvm:ear"))
    implementation(project(":distribution-core:tooling-api"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.inject)

    testFixturesApi(project(":distribution-core:base-services")) {
        because("test fixtures export the Action class")
    }
    testFixturesApi(project(":distribution-core:logging")) {
        because("test fixtures export the ConsoleOutput class")
    }
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))

    testImplementation(project(":distribution-plugins:core:dependency-management"))
    testImplementation(libs.xmlunit)
    testImplementation(libs.equalsverifier)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))

    integTestImplementation(libs.jetty)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm"))
}

strictCompile {
    ignoreRawTypes()
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/plugins/ide/internal/*",
        "org/gradle/plugins/ide/eclipse/internal/*",
        "org/gradle/plugins/ide/idea/internal/*",
        "org/gradle/plugins/ide/eclipse/model/internal/*",
        "org/gradle/plugins/ide/idea/model/internal/*"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/ide/src/main")
