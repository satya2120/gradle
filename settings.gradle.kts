import org.gradle.api.internal.FeaturePreviews

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

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    }
}

plugins {
    id("com.gradle.enterprise").version("3.4.1")
    id("com.gradle.enterprise.gradle-enterprise-conventions-plugin").version("0.7.1")
}

apply(from = "gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")

// If you include a new subproject here, you will need to execute the
// ./gradlew generateSubprojectsInfo
// task to update metadata about the build for CI

// Gradle Distributions - for testing and for publishing a full distribution
includeAll("distribution-setup")

// Gradle implementation projects
includeAll("distribution-core")
includeAll("distribution-plugins/core")
includeAll("distribution-plugins/basics")
includeAll("distribution-plugins/jvm")
includeAll("distribution-plugins/native")
includeAll("distribution-plugins/publishing")
includeAll("distribution-plugins/full")

// Plugin portal projects
includeAll("portal-plugins")

// Internal utility and verification projects
includeAll("documentation")
includeAll("fixtures")
includeAll("code-quality")
includeAll("end-2-end-tests")

val upperCaseLetters = "\\p{Upper}".toRegex()

rootProject.name = "gradle"

fun includeAll(folder : String) {
    file(folder).listFiles()!!.filter { it.isDirectory }.forEach { projectDir ->
        val projectName = projectDir.name
        include(projectName)
        
        val project = rootProject.children.first { it.name == projectName }
        project.projectDir = projectDir
        project.buildFileName = buildFileNameFor(projectName)
    }
}

fun buildFileNameFor(projectDirName: String) =
    "$projectDirName${buildFileExtensionFor(projectDirName)}"

fun buildFileExtensionFor(projectDirName: String) =
    if (projectDirName == "docs") ".gradle" else ".gradle.kts"

FeaturePreviews.Feature.values().forEach { feature ->
    if (feature.isActive) {
        enableFeaturePreview(feature.name)
    }
}
