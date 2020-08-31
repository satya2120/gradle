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
import gradlebuild.integrationtests.tasks.IntegrationTest

plugins {
    id("gradlebuild.distribution.api-java")
}

val integTestRuntimeResources by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = false
}
val integTestRuntimeResourcesClasspath by configurations.creating {
    extendsFrom(integTestRuntimeResources)
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        // play test apps MUST be found as exploded directory
        attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements::class.java, LibraryElements.RESOURCES))
    }
    isTransitive = false
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-plugins:jvm:ide"))
    implementation(project(":distribution-plugins:jvm:language-scala"))
    implementation(project(":distribution-plugins:core:platform-base"))
    implementation(project(":distribution-plugins:core:platform-jvm"))
    implementation(project(":distribution-plugins:full:platform-play"))

    implementation(libs.groovy)
    implementation(libs.guava)

    integTestImplementation(testFixtures(project(":distribution-plugins:full:platform-play")))
    integTestImplementation(testFixtures(project(":distribution-plugins:jvm:ide")))

    integTestRuntimeResources(testFixtures(project(":distribution-plugins:full:platform-play")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}

strictCompile {
    ignoreDeprecations() // Play support in Gradle core has been deprecated
}

tasks.withType<IntegrationTest>().configureEach {
    dependsOn(":distribution-plugins:full:platform-play:integTestPrepare")
    // this is a workaround for which we need a better fix:
    // it sets the platform play test fixtures :distribution-core:resources directory in front
    // of the classpath, so that we can find them when executing tests in
    // an exploded format, rather than finding them in the test fixtures jar
    classpath = integTestRuntimeResourcesClasspath + classpath
}
