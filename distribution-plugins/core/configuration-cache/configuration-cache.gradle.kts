import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("gradlebuild.distribution.implementation-kotlin")
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += listOf(
                "-XXLanguage:+NewInference",
                "-XXLanguage:+SamConversionForKotlinFunctions"
            )
        }
    }

    processResources {
        from({ project(":code-quality:configuration-cache-report").tasks.named("assembleReport") }) {
            into("org/gradle/configurationcache")
        }
    }

    configCacheIntegTest {
        enabled = false
    }
}

afterEvaluate {
    // This is a workaround for the validate plugins task trying to inspect classes which have changed but are NOT tasks.
    // For the current project, we simply disable it since there are no tasks in there.
    tasks.withType<ValidatePlugins>().configureEach {
        enabled = false
    }
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:base-services-groovy"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-core:execution"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:kotlin-dsl"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:persistent-cache"))
    implementation(project(":distribution-plugins:core:plugins"))
    implementation(project(":distribution-plugins:core:publish"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-core:snapshots"))
    implementation(project(":distribution-plugins:core:plugin-use"))

    // TODO - move the isolatable serializer to :distribution-core:model-core to live with the isolatable infrastructure
    implementation(project(":distribution-plugins:core:workers"))

    // TODO - it might be good to allow projects to contribute state to save and restore, rather than have this project know about everything
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-core:build-events"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:build-option"))
    implementation(project(":distribution-plugins:core:platform-jvm"))

    implementation(libs.groovy)
    implementation(libs.slf4jApi)
    implementation(libs.guava)

    implementation(libs.futureKotlin("stdlib-jdk8"))
    implementation(libs.futureKotlin("reflect"))

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(libs.mockitoKotlin2)
    testImplementation(libs.kotlinCoroutinesDebug)

    integTestImplementation(project(":distribution-core:jvm-services"))
    integTestImplementation(project(":distribution-core:tooling-api"))
    integTestImplementation(project(":distribution-plugins:core:platform-jvm"))
    integTestImplementation(project(":distribution-plugins:basics:test-kit"))
    integTestImplementation(project(":distribution-core:launcher"))

    integTestImplementation(libs.guava)
    integTestImplementation(libs.ant)
    integTestImplementation(libs.inject)
    integTestImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    integTestImplementation(testFixtures(project(":distribution-plugins:jvm:jacoco")))

    crossVersionTestImplementation(project(":distribution-core:cli"))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm")) {
        because("Includes tests for builds with TestKit involved; ConfigurationCacheJacocoIntegrationTest requires JVM distribution")
    }
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}

classycle {
    excludePatterns.set(listOf("org/gradle/configurationcache/**"))
}
