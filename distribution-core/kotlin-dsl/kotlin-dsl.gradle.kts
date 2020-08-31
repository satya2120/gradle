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
    id("gradlebuild.distribution.api-kotlin")
    id("gradlebuild.kotlin-dsl-dependencies-embedded")
}

description = "Kotlin DSL Provider"

dependencies {
    api(project(":distribution-core:kotlin-dsl-tooling-models"))
    api(project(":distribution-core:kotlin-compiler-embeddable"))
    api(libs.futureKotlin("stdlib-jdk8"))

    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:base-services-groovy")) // for 'Specs'
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-core:execution"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.inject)

    implementation(libs.futureKotlin("scripting-common")) {
        isTransitive = false
    }
    implementation(libs.futureKotlin("scripting-jvm")) {
        isTransitive = false
    }
    implementation(libs.futureKotlin("scripting-jvm-host-embeddable")) {
        isTransitive = false
    }
    implementation(libs.futureKotlin("scripting-compiler-embeddable")) {
        isTransitive = false
    }
    implementation(libs.futureKotlin("scripting-compiler-impl-embeddable")) {
        isTransitive = false
    }
    implementation(libs.futureKotlin("sam-with-receiver-compiler-plugin")) {
        isTransitive = false
    }
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.1.0") {
        isTransitive = false
    }

    testImplementation(project(":distribution-plugins:basics:build-cache-http"))
    testImplementation(project(":distribution-plugins:full:build-init"))
    testImplementation(project(":distribution-plugins:jvm:jacoco"))
    testImplementation(project(":distribution-plugins:native:platform-native")) {
        because("BuildType from platform-native is used in ProjectAccessorsClassPathTest")
    }
    testImplementation(project(":distribution-plugins:core:plugins"))
    testImplementation(project(":distribution-plugins:core:version-control"))
    testImplementation(libs.ant)
    testImplementation(libs.asm)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.jacksonKotlin)

    testImplementation(libs.archunit)
    testImplementation(libs.kotlinCoroutines)
    testImplementation(libs.awaitility)

    integTestImplementation(project(":distribution-plugins:core:language-groovy"))
    integTestImplementation(project(":distribution-plugins:core:language-groovy")) {
        because("ClassBytesRepositoryTest makes use of Groovydoc task.")
    }
    integTestImplementation(project(":fixtures:internal-testing"))
    integTestImplementation(libs.mockitoKotlin)

    testRuntimeOnly(project(":distribution-setup:distributions-native")) {
        because("SimplifiedKotlinScriptEvaluator reads default imports from the distribution (default-imports.txt) and BuildType from platform-native is used in ProjectAccessorsClassPathTest.")
    }

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(project(":distribution-core:core-api"))
    testFixturesImplementation(project(":distribution-core:core"))
    testFixturesImplementation(project(":distribution-core:resources"))
    testFixturesImplementation(project(":distribution-plugins:basics:kotlin-dsl-tooling-builders"))
    testFixturesImplementation(project(":distribution-plugins:basics:test-kit"))
    testFixturesImplementation(project(":fixtures:internal-testing"))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))

    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.mockitoKotlin)
    testFixturesImplementation(libs.jacksonKotlin)
    testFixturesImplementation(libs.asm)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/kotlin/dsl/**"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
