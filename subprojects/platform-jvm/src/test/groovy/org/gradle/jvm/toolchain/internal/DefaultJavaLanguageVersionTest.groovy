/*
 * Copyright 2020 the original author or authors.
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

package org.gradle.jvm.toolchain.internal

import org.gradle.jvm.toolchain.JavaLanguageVersion
import spock.lang.Specification

import java.util.Map.Entry

class DefaultJavaLanguageVersionTest extends Specification {

    def 'defines known versions'() {
        given:
        Map<Integer, JavaLanguageVersion> knownVersions = DefaultJavaLanguageVersion.KNOWN_VERSIONS

        expect:
        for (Entry<Integer, JavaLanguageVersion> entry : knownVersions.entrySet()) {
            assert entry.value.asInt() == entry.key
        }
    }

    def 'behaves as an integer wrapper'() {
        given:
        def value = new Random().nextInt()

        when:
        def version = new DefaultJavaLanguageVersion(value)

        then:
        version.asInt() == value
        version.asString() == String.valueOf(value)
        version.toString() == String.valueOf(value)
    }

    def 'compatibility relates to sort order'() {
        given:
        def rand = new Random()
        def firstValue = rand.nextInt()
        def secondValue = rand.nextInt()

        when:
        def firstVersion = new DefaultJavaLanguageVersion(firstValue)
        def secondVersion = new DefaultJavaLanguageVersion(secondValue)

        then:
        firstVersion.interoperatesWith(secondVersion) == firstVersion >= secondVersion
        secondVersion.interoperatesWith(firstVersion) == secondVersion >= firstVersion
    }
}
