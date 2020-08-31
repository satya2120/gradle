import gradlebuild.cleanup.WhenNotEmpty
/*
 * Copyright 2012 the original author or authors.
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
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:build-option"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.inject)
    implementation(libs.jatl)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-core:logging")))

    integTestImplementation(libs.jsoup)
    integTestImplementation(libs.jetty)

    testFixturesApi(testFixtures(project(":distribution-plugins:native:platform-native")))
    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(libs.guava)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))  {
        because("There are integration tests that assert that all the tasks of a full distribution are reported (these should probably move to ':integTests').")
    }
}

classycle {
    excludePatterns.set(listOf(
        "org/gradle/api/reporting/model/internal/*",
        "org/gradle/api/reporting/dependencies/internal/*",
        "org/gradle/api/plugins/internal/*"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
