/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.api.tasks.compile

import org.gradle.api.JavaVersion
import org.gradle.integtests.fixtures.AbstractPluginIntegrationTest
import org.gradle.integtests.fixtures.AvailableJavaHomes
import org.gradle.internal.jvm.Jvm
import org.gradle.util.Requires
import spock.lang.IgnoreIf
import spock.lang.Unroll

class JavaCompileToolchainIntegrationTest extends AbstractPluginIntegrationTest {

    @Unroll
    @IgnoreIf({ AvailableJavaHomes.differentJdk == null })
    def "can manually set java compiler via #type toolchain on java compile task"() {
        buildFile << """
            apply plugin: "java"

            compileJava {
                javaCompiler = javaToolchains.compilerFrom {
                    languageVersion = JavaLanguageVersion.of(${jdk.javaVersion.majorVersion})
                }
            }
        """

        file("src/main/java/Foo.java") << "public class Foo {}"

        when:
        runWithToolchainConfigured(jdk)

        then:
        outputContains("Compiling with toolchain '${jdk.javaHome.absolutePath}'.")
        javaClassFile("Foo.class").exists()

        where:
        type           | jdk
        'differentJdk' | AvailableJavaHomes.getDifferentJdk()
        'current'      | Jvm.current()
    }

    def "can set explicit toolchain used by JavaCompile"() {
        def someJdk = AvailableJavaHomes.getDifferentJdk()
        buildFile << """
            apply plugin: "java"

            java {
                toolchain {
                    languageVersion = JavaLanguageVersion.of(${someJdk.javaVersion.majorVersion})
                }
            }
        """

        file("src/main/java/Foo.java") << "public class Foo {}"

        when:
        runWithToolchainConfigured(someJdk)

        then:
        outputContains("Compiling with toolchain '${someJdk.javaHome.absolutePath}'.")
        javaClassFile("Foo.class").exists()
    }

    @Requires(adhoc = { AvailableJavaHomes.getJdk(JavaVersion.VERSION_11) != null })
    def "uses matching compatibility options for source and target level"() {
        def jdk11 = AvailableJavaHomes.getJdk(JavaVersion.VERSION_11)
        buildFile << """
            apply plugin: "java"

            java {
                toolchain {
                    languageVersion = JavaLanguageVersion.of(11)
                }
            }
        """

        file("src/main/java/Foo.java") << """
public class Foo {
    public void foo() {
        java.util.function.Function<String, String> append = (var string) -> string + " ";
    }
}
"""

        when:
        runWithToolchainConfigured(jdk11)

        then:
        outputContains("Compiling with toolchain '${jdk11.javaHome.absolutePath}'.")
        javaClassFile("Foo.class").exists()
    }

    def runWithToolchainConfigured(Jvm jvm) {
        result = executer
            .withArgument("-Porg.gradle.java.installations.auto-detect=false")
            .withArgument("-Porg.gradle.java.installations.paths=" + jvm.javaHome.absolutePath)
            .withArgument("--info")
            .withTasks("compileJava")
            .run()
    }

}
