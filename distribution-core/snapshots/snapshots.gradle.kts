/*
 * Copyright 2018 the original author or authors.
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
    id("gradlebuild.distribution.implementation-java")
    id("gradlebuild.publish-public-libraries")
}

description = "Tools to take immutable, comparable :distribution-core:snapshots of files and other things"

dependencies {
    api(project(":distribution-core:files"))
    api(project(":distribution-core:hashing"))

    implementation(project(":distribution-core:base-annotations"))

    implementation(libs.guava)
    implementation(libs.slf4jApi)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:resources"))
    testImplementation(project(":distribution-core:native"))
    testImplementation(project(":distribution-core:persistent-cache"))
    testImplementation(libs.ant)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:core-api")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-core:file-collections")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:file-collections"))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

afterEvaluate {
    // This is a workaround for the validate plugins task trying to inspect classes which have changed but are NOT tasks.
    // For the current project, we exclude all internal packages, since there are no tasks in there.
    tasks.withType<ValidatePlugins>().configureEach {
        classes.setFrom(sourceSets.main.get().output.classesDirs.asFileTree.matching { exclude("**/internal/**") })
    }
}
