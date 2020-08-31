/*
 * Copyright 2016 the original author or authors.
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
    implementation(project(":distribution-core:build-cache"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:basics:resources-http"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsHttpclient)
    implementation(libs.commonsLang)
    implementation(libs.inject)

    testImplementation(testFixtures(project(":distribution-core:core")))

    integTestImplementation(libs.jetty)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}
