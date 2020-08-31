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

plugins {
    id("gradlebuild.distribution.api-java")
}

configurations {
    register("reports")
}

tasks.classpathManifest {
    optionalProjects.add("gradle-kotlin-dsl")
    // The gradle-runtime-api-info.jar is added by a 'distributions-...' project if it is on the (integration test) runtime classpath.
    // It contains information services in ':distribution-core:core' need to reason about the complete Gradle distribution.
    // To allow parts of ':distribution-core:core' code to be instantiated in unit tests without relying on this functionality, the dependency is optional.
    optionalProjects.add("gradle-runtime-api-info")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:cli"))
    implementation(project(":distribution-core:build-option"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:build-cache-packaging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-core:model-groovy"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-core:file-watching"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-core:worker-processes"))
    implementation(project(":distribution-core:normalization-java"))

    implementation(libs.groovy)
    implementation(libs.ant)
    implementation(libs.guava)
    implementation(libs.inject)
    implementation(libs.asm)
    implementation(libs.asmCommons)
    implementation(libs.slf4jApi)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.nativePlatform)
    implementation(libs.nativePlatformFileEvents)
    implementation(libs.commonsCompress)
    implementation(libs.xmlApis)

    testImplementation(project(":distribution-plugins:core:plugins"))
    testImplementation(project(":distribution-plugins:core:testing-base"))
    testImplementation(project(":distribution-plugins:native:platform-native"))
    testImplementation(libs.jsoup)
    testImplementation(libs.log4jToSlf4j)
    testImplementation(libs.jclToSlf4j)

    testFixturesApi(project(":distribution-core:base-services")) {
        because("test fixtures expose Action")
    }
    testFixturesApi(project(":distribution-core:base-services-groovy")) {
        because("test fixtures expose AndSpec")
    }
    testFixturesApi(project(":distribution-core:core-api")) {
        because("test fixtures expose Task")
    }
    testFixturesApi(project(":distribution-core:logging")) {
        because("test fixtures expose Logger")
    }
    testFixturesApi(project(":distribution-core:model-core")) {
        because("test fixtures expose IConventionAware")
    }
    testFixturesApi(project(":distribution-core:build-cache")) {
        because("test fixtures expose BuildCacheController")
    }
    testFixturesApi(project(":distribution-core:execution")) {
        because("test fixtures expose OutputChangeListener")
    }
    testFixturesApi(project(":distribution-core:native")) {
        because("test fixtures expose FileSystem")
    }
    testFixturesImplementation(project(":distribution-core:file-collections"))
    testFixturesImplementation(project(":distribution-core:native"))
    testFixturesImplementation(project(":distribution-core:resources"))
    testFixturesImplementation(project(":distribution-core:process-services"))
    testFixturesImplementation(project(":distribution-core:messaging"))
    testFixturesImplementation(project(":distribution-core:persistent-cache"))
    testFixturesImplementation(project(":distribution-core:snapshots"))
    testFixturesImplementation(project(":distribution-core:normalization-java"))
    testFixturesImplementation(libs.ivy)
    testFixturesImplementation(libs.slf4jApi)
    testFixturesImplementation(libs.guava)
    testFixturesImplementation(libs.ant)

    testFixturesRuntimeOnly(project(":distribution-plugins:core:plugin-use")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    testFixturesRuntimeOnly(project(":distribution-plugins:core:dependency-management")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    testFixturesRuntimeOnly(project(":distribution-plugins:core:workers")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    testFixturesRuntimeOnly(project(":distribution-plugins:core:composite-builds")) {
        because("We always need a BuildStateRegistry service implementation")
    }

    testImplementation(project(":distribution-plugins:core:dependency-management"))

    testImplementation(testFixtures(project(":distribution-core:core-api")))
    testImplementation(testFixtures(project(":distribution-core:messaging")))
    testImplementation(testFixtures(project(":distribution-core:model-core")))
    testImplementation(testFixtures(project(":distribution-core:logging")))
    testImplementation(testFixtures(project(":distribution-core:base-services")))
    testImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))

    integTestImplementation(project(":distribution-plugins:core:workers"))
    integTestImplementation(project(":distribution-plugins:core:dependency-management"))
    integTestImplementation(project(":distribution-core:launcher"))
    integTestImplementation(project(":distribution-plugins:core:plugins"))
    integTestImplementation(libs.jansi)
    integTestImplementation(libs.jetbrainsAnnotations)
    integTestImplementation(libs.jetty)
    integTestImplementation(libs.littleproxy)
    integTestImplementation(testFixtures(project(":distribution-core:native")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics")) {
        because("Some tests utilise the 'java-gradle-plugin' and with that TestKit")
    }
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

tasks.test {
    setForkEvery(200)
}

tasks.compileTestGroovy {
    groovyOptions.fork("memoryInitialSize" to "128M", "memoryMaximumSize" to "1G")
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
