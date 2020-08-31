// Defines which JARs go into the core part (libs/*.jar) of a Gradle distribution (NOT libs/plugins/*.jar).
plugins {
    id("gradlebuild.platform")
}

javaPlatform.allowDependencies()

dependencies {
    runtime(project(":distribution-core:installation-beacon"))
    runtime(project(":distribution-core:api-metadata"))
    runtime(project(":distribution-core:launcher")) {
        because("This is the entry point of Gradle core which transitively depends on all other core projects.")
    }
    runtime(project(":distribution-core:kotlin-dsl")) {
        because("Adds support for Kotlin DSL scripts.")
    }
}
