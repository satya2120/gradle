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
import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.getIncludeCategories
import org.gradle.api.internal.runtimeshaded.PackageListGenerator

plugins {
    id("gradlebuild.distribution.implementation-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:wrapper"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(libs.commonsIo)

    testImplementation(libs.guava)
    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestImplementation(project(":distribution-core:native"))
    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(project(":distribution-core:launcher"))
    integTestImplementation(project(":distribution-core:build-option"))
    integTestImplementation(project(":distribution-core:jvm-services"))
    integTestImplementation(libs.slf4jApi)

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}

val generateTestKitPackageList by tasks.registering(PackageListGenerator::class) {
    classpath = sourceSets.main.get().runtimeClasspath
    outputFile = file(layout.buildDirectory.file("runtime-api-info/test-kit-relocated.txt"))
}
tasks.jar {
    into("org/gradle/api/internal/runtimeshaded") {
        from(generateTestKitPackageList)
    }
}

classycle {
    excludePatterns.set(listOf("org/gradle/testkit/runner/internal/**"))
}

tasks.integMultiVersionTest {
    systemProperty("org.gradle.integtest.testkit.compatibility", "all")
    // TestKit multi version tests are not using JUnit categories
    getIncludeCategories().clear()
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
