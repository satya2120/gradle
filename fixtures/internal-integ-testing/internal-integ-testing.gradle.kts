/*
 * Copyright 2011 the original author or authors.
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
import gradlebuild.basics.util.ReproduciblePropertiesWriter
import java.util.Properties

plugins {
    id("gradlebuild.internal.java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:cli"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:files"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:configuration-cache"))
    implementation(project(":distribution-core:jvm-services"))
    implementation(project(":distribution-core:launcher"))
    implementation(project(":fixtures:internal-testing"))
    implementation(project(":distribution-core:build-events"))
    implementation(project(":distribution-core:build-option"))

    implementation(libs.groovy)
    implementation(libs.junit)
    implementation(libs.spock)
    implementation(libs.nativePlatform)
    implementation(libs.commonsLang)
    implementation(libs.commonsIo)
    implementation(libs.jetty)
    implementation(libs.littleproxy)
    implementation(libs.gcs)
    implementation(libs.inject)
    implementation(libs.commonsHttpclient)
    implementation(libs.joda)
    implementation(libs.jacksonCore)
    implementation(libs.jacksonAnnotations)
    implementation(libs.jacksonDatabind)
    implementation(libs.ivy)
    implementation(libs.ant)
    implementation(libs.jgit) {
        because("Some tests require a git reportitory - see AbstractIntegrationSpec.initGitDir(")
    }

    // we depend on both: sshd platforms and libraries
    implementation(libs.sshdCore)
    implementation(platform(libs.sshdCore))
    implementation(libs.sshdScp)
    implementation(platform(libs.sshdScp))
    implementation(libs.sshdSftp)
    implementation(platform(libs.sshdSftp))

    implementation(libs.gson)
    implementation(libs.joda)
    implementation(libs.jsch)
    implementation(libs.jcifs)
    implementation(libs.jansi)
    implementation(libs.ansiControlSequenceUtil)
    implementation(libs.mina)
    implementation(libs.sampleCheck) {
        exclude(module = "groovy-all")
        exclude(module = "slf4j-simple")
    }
    implementation(testFixtures(project(":distribution-core:core")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/**"))
}

val prepareVersionsInfo = tasks.register<PrepareVersionsInfo>("prepareVersionsInfo") {
    destFile.set(layout.buildDirectory.file("generated-resources/all-released-versions/all-released-versions.properties"))
    versions.set(moduleIdentity.releasedVersions.map {
        it.allPreviousVersions.joinToString(" ") { it.version }
    })
    mostRecent.set(moduleIdentity.releasedVersions.map { it.mostRecentRelease.version })
    mostRecentSnapshot.set(moduleIdentity.releasedVersions.map { it.mostRecentSnapshot.version })
}

val copyAgpVersionsInfo by tasks.registering(Copy::class) {
    from(rootProject.layout.projectDirectory.file("gradle/dependency-management/agp-versions.properties"))
    into(layout.buildDirectory.dir("generated-resources/agp-versions"))
}

sourceSets.main {
    output.dir(prepareVersionsInfo.map { it.destFile.get().asFile.parentFile })
    output.dir(copyAgpVersionsInfo)
}

@CacheableTask
abstract class PrepareVersionsInfo : DefaultTask() {

    @get:OutputFile
    abstract val destFile: RegularFileProperty

    @get:Input
    abstract val mostRecent: Property<String>

    @get:Input
    abstract val versions: Property<String>

    @get:Input
    abstract val mostRecentSnapshot: Property<String>

    @TaskAction
    fun prepareVersions() {
        val properties = Properties()
        properties["mostRecent"] = mostRecent.get()
        properties["mostRecentSnapshot"] = mostRecentSnapshot.get()
        properties["versions"] = versions.get()
        ReproduciblePropertiesWriter.store(properties, destFile.get().asFile)
    }
}
