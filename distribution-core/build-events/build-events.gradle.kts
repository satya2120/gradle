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

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:tooling-api"))

    implementation(libs.jsr305)
    implementation(libs.guava)

    testImplementation(project(":fixtures:internal-testing"))
    testImplementation(project(":distribution-core:model-core"))

    integTestImplementation(project(":distribution-core:logging")) {
        because("This isn't declared as part of integtesting's API, but should be as logging's classes are in fact visible on the API")
    }
    integTestImplementation(project(":distribution-core:build-option"))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))  {
        because("Requires ':toolingApiBuilders': Event handlers are in the wrong place, and should live in this project")
    }
}
