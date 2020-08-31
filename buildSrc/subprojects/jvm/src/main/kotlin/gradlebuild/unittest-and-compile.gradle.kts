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
package gradlebuild

import gradlebuild.basics.accessors.groovy
import gradlebuild.basics.BuildEnvironment
import gradlebuild.basics.tasks.ClasspathManifest
import gradlebuild.basics.extension.vendorAndMajorVersion
import gradlebuild.jvm.argumentproviders.CiEnvironmentProvider
import gradlebuild.jvm.extension.UnitTestAndCompileExtension
import org.gradle.internal.os.OperatingSystem
import java.util.concurrent.Callable
import java.util.jar.Attributes
import com.gradle.enterprise.gradleplugin.testdistribution.TestDistributionPlugin

plugins {
    groovy
    id("gradlebuild.module-identity")
    id("gradlebuild.dependency-modules")
    id("gradlebuild.available-java-installations")
    id("org.gradle.test-retry")
}

extensions.create<UnitTestAndCompileExtension>("gradlebuildJava", java)

removeTeamcityTempProperty()
addDependencies()
configureClasspathManifestGeneration()
configureCompile()
configureSourcesVariant()
configureJarTasks()
configureTests()

fun configureCompile() {
    java.targetCompatibility = JavaVersion.VERSION_1_8
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    afterEvaluate {
        val jdkForCompilation = buildJvms.compileJvm.get()

        tasks.withType<JavaCompile>().configureEach {
            configureCompileTask(this, options, jdkForCompilation)
        }
        tasks.withType<GroovyCompile>().configureEach {
            groovyOptions.encoding = "utf-8"
            configureCompileTask(this, options, jdkForCompilation)
        }
    }
    addCompileAllTask()
}

fun configureSourcesVariant() {
    java {
        withSourcesJar()
    }
    val implementation by configurations

    @Suppress("unused_variable")
    val transitiveSourcesElements by configurations.creating {
        isCanBeResolved = false
        isCanBeConsumed = true
        extendsFrom(implementation)
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
            attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("gradle-source-folders"))
        }
        val main = sourceSets.main.get()
        main.java.srcDirs.forEach {
            outgoing.artifact(it)
        }
        main.groovy.srcDirs.forEach {
            outgoing.artifact(it)
        }
    }
}

fun configureCompileTask(compileTask: AbstractCompile, options: CompileOptions, jdkForCompilation: JavaInstallation) {
    options.isFork = true
    options.encoding = "utf-8"
    options.isIncremental = true
    options.forkOptions.jvmArgs?.add("-XX:+HeapDumpOnOutOfMemoryError")
    options.forkOptions.memoryMaximumSize = "1g"
    options.compilerArgs.addAll(mutableListOf("-Xlint:-options", "-Xlint:-path"))
    compileTask.inputs.property("javaInstallation", Callable {
        jdkForCompilation.vendorAndMajorVersion()
    })
}

fun configureClasspathManifestGeneration() {
    val runtimeClasspath by configurations
    val classpathManifest = tasks.register("classpathManifest", ClasspathManifest::class) {
        this.runtimeClasspath.from(runtimeClasspath)
        this.externalDependencies.from(runtimeClasspath.fileCollection { it is ExternalDependency })
        this.manifestFile.set(moduleIdentity.baseName.map { layout.buildDirectory.file("generated-resources/$it-classpath/$it-classpath.properties").get() })
    }
    sourceSets.main.get().output.dir(
        classpathManifest.map { it.manifestFile.get().asFile.parentFile }
    )
}

fun addDependencies() {
    dependencies {
        testCompileOnly(libs.junit)
        testRuntimeOnly(libs.junit5Vintage)
        testImplementation(libs.groovy)
        testImplementation(libs.spock)
        testRuntimeOnly(libs.bytebuddy)
        testRuntimeOnly(libs.objenesis)

        // use a separate configuration for the platform dependency that does not get published as part of 'apiElements' or 'runtimeElements'
        val platformImplementation by configurations.creating
        configurations["compileClasspath"].extendsFrom(platformImplementation)
        configurations["runtimeClasspath"].extendsFrom(platformImplementation)
        configurations["testCompileClasspath"].extendsFrom(platformImplementation)
        configurations["testRuntimeClasspath"].extendsFrom(platformImplementation)
        platformImplementation.withDependencies {
            // use 'withDependencies' to not attempt to find platform project during script compilation
            add(project.dependencies.create(platform(project(":distribution-setup:distributions-dependencies"))))
        }
    }
}

fun addCompileAllTask() {
    tasks.register("compileAll") {
        val compileTasks = project.tasks.matching {
            it is JavaCompile || it is GroovyCompile
        }
        dependsOn(compileTasks)
    }
}

fun configureJarTasks() {
    tasks.withType<Jar>().configureEach {
        archiveBaseName.set(moduleIdentity.baseName)
        archiveVersion.set(moduleIdentity.version.map { it.baseVersion.version })
        manifest.attributes(mapOf(
            Attributes.Name.IMPLEMENTATION_TITLE.toString() to "Gradle",
            Attributes.Name.IMPLEMENTATION_VERSION.toString() to moduleIdentity.version.map { it.baseVersion.version }))
    }
}

fun Test.configureJvmForTest() {
    val jvmForTest = project.buildJvms.testJvm.get()

    jvmArgumentProviders.add(CiEnvironmentProvider(this))
    executable = jvmForTest.javaExecutable.asFile.absolutePath
    environment["JAVA_HOME"] = jvmForTest.installationDirectory.asFile.absolutePath
    if (jvmForTest.javaVersion.isJava7) {
        // enable class unloading
        jvmArgs("-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled")
    }
    if (jvmForTest.javaVersion.isJava9Compatible) {
        // allow embedded executer to modify environment variables
        jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
        // allow embedded executer to inject legacy types into the system classloader
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }
    // Includes JVM vendor and major version
    inputs.property("javaInstallation", Callable { jvmForTest.vendorAndMajorVersion() })
}

fun Test.addOsAsInputs() {
    // Add OS as inputs since tests on different OS may behave differently https://github.com/gradle/gradle-private/issues/2831
    // the version currently differs between our dev infrastructure, so we only track the name and the architecture
    inputs.property("operatingSystem", "${OperatingSystem.current().name} ${System.getProperty("os.arch")}")
}

fun Project.testDistributionEnabled() = providers.systemProperty("enableTestDistribution").forUseAtConfigurationTime().orNull?.toBoolean() == true

fun configureTests() {
    normalization {
        runtimeClasspath {
            // Ignore the build receipt as it is not relevant for tests and changes between each execution
            ignore("org/gradle/build-receipt.properties")
        }
    }

    if (project.testDistributionEnabled()) {
        plugins.apply(TestDistributionPlugin::class.java)
    }

    tasks.withType<Test>().configureEach {
        filterEnvironmentVariables()

        maxParallelForks = project.maxParallelForks

        if (!BuildEnvironment.isIntelliJIDEA) {
            // JUnit 5 Vintage engine can't recognize Spock @Unroll test method correctly
            // So if running an @Unroll method in IDEA with include pattern "SomeClass.methodName"
            // The result will be incorrect. In this case we fallback to JUnit
            useJUnitPlatform()
        }
        configureJvmForTest()
        addOsAsInputs()

        val testName = name

        if (BuildEnvironment.isCiServer) {
            retry {
                maxRetries.convention(1)
                maxFailures.set(10)
            }
            doFirst {
                logger.lifecycle("maxParallelForks for '$path' is $maxParallelForks")
            }
        }

        if (project.testDistributionEnabled()) {
            distribution {
                maxLocalExecutors.set(0)
                maxRemoteExecutors.set(if ("test" == testName) 5 else 20)
                enabled.set(true)
                when {
                    OperatingSystem.current().isLinux -> requirements.set(listOf("os=linux"))
                    OperatingSystem.current().isWindows -> requirements.set(listOf("os=windows"))
                    OperatingSystem.current().isMacOsX -> requirements.set(listOf("os=macos"))
                }
            }
        }
    }
}

fun removeTeamcityTempProperty() {
    // Undo: https://github.com/JetBrains/teamcity-gradle/blob/e1dc98db0505748df7bea2e61b5ee3a3ba9933db/gradle-runner-agent/src/main/scripts/init.gradle#L818
    if (project.hasProperty("teamcity")) {
        @Suppress("UNCHECKED_CAST")
        val teamcity = project.property("teamcity") as MutableMap<String, Any>
        teamcity["teamcity.build.tempDir"] = ""
    }
}

val Project.maxParallelForks: Int
    get() = findProperty("maxParallelForks")?.toString()?.toInt() ?: 4
