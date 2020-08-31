/*
 * Copyright 2012 the original author or authors.
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

    implementation(libs.groovy)
    implementation(libs.guava)

    testImplementation(testFixtures(project(":distribution-core:core")))
}

strictCompile {
    ignoreParameterizedVarargType() // [unchecked] Possible heap pollution from parameterized vararg type: org.gradle.api.specs.AndSpec.and()
}
