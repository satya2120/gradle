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
plugins {
    id("gradlebuild.distribution.api-java")
}

description = "Shared classes for projects requiring GPG support"

dependencies {
    api(project(":distribution-core:core-api"))
    api(project(":distribution-core:resources"))
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-plugins:basics:resources-http"))
    implementation(libs.guava)

    api(libs.bouncycastlePgp)

    implementation(libs.groovy) {
        because("Project.exec() depends on Groovy")
    }

    testImplementation(testFixtures(project(":distribution-core:core")))

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(libs.slf4jApi)
    testFixturesImplementation(libs.jetty)
    testFixturesImplementation(testFixtures(project(":distribution-core:core")))
    testFixturesImplementation(project(":fixtures:internal-integ-testing"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
}

classycle {
    excludePatterns.set(listOf("org/gradle/plugins/signing/type/pgp/**"))
}
