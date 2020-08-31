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

import gradlebuild.cleanup.WhenNotEmpty

plugins {
    id("gradlebuild.distribution.implementation-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:basics:resources-http"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-plugins:core:security"))

    implementation(libs.slf4jApi)
    implementation(libs.groovy)
    implementation(libs.asm)
    implementation(libs.asmCommons)
    implementation(libs.asmUtil)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.commonsHttpclient)
    implementation(libs.inject)
    implementation(libs.gson)
    implementation(libs.ant)
    implementation(libs.ivy)
    implementation(libs.maven3)

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-plugins:core:diagnostics"))
    testImplementation(project(":distribution-core:build-cache-packaging"))
    testImplementation(libs.nekohtml)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:core-api")))
    testImplementation(testFixtures(project(":distribution-plugins:core:version-control")))
    testImplementation(testFixtures(project(":distribution-plugins:basics:resources-http")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-core:snapshots")))
    testImplementation(testFixtures(project(":distribution-core:execution")))

    integTestImplementation(project(":distribution-core:build-option"))
    integTestImplementation(libs.jansi)
    integTestImplementation(libs.ansiControlSequenceUtil)
    integTestImplementation(libs.jetty) {
        because("tests use HttpServlet directly")
    }
    integTestImplementation(testFixtures(project(":distribution-plugins:core:security")))

    testFixturesApi(project(":distribution-core:base-services")) {
        because("Test fixtures export the Action class")
    }
    testFixturesApi(project(":distribution-core:persistent-cache")) {
        because("Test fixtures export the CacheAccess class")
    }

    testFixturesApi(libs.jetty)
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(testFixtures(project(":distribution-plugins:basics:resources-http")))
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:messaging"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))
    testFixturesImplementation(libs.slf4jApi)
    testFixturesImplementation(libs.inject)
    testFixturesImplementation(libs.guava) {
        because("Groovy compiler reflects on private field on TextUtil")
    }
    testFixturesImplementation(libs.bouncycastlePgp)
    testFixturesApi(libs.testcontainersSpock) {
        because("API because of Groovy compiler bug leaking internals")
    }
    testFixturesImplementation(project(":distribution-core:jvm-services")) {
        because("Groovy compiler bug leaks internals")
    }

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

tasks.clean {
    val testFiles = layout.buildDirectory.dir("tmp/test files")
    doFirst {
        // On daemon crash, read-only cache tests can leave read-only files around.
        // clean now takes care of those files as well
        testFiles.get().asFileTree.matching {
            include("**/read-only-cache/**")
        }.visit { this.file.setWritable(true) }
    }
}

afterEvaluate {
    // This is a workaround for the validate plugins task trying to inspect classes which
    // have changed but are NOT tasks
    tasks.withType<ValidatePlugins>().configureEach {
        val main = sourceSets.main.get()
        classes.setFrom(main.output.classesDirs.asFileTree.filter { !it.isInternal(main) })
    }
}

fun File.isInternal(sourceSet: SourceSet) = isInternal(sourceSet.output.classesDirs.files)

fun File.isInternal(roots: Set<File>): Boolean = name == "internal" ||
    !roots.contains(parentFile) && parentFile.isInternal(roots)
