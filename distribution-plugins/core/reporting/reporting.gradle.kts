import gradlebuild.basics.googleApisJs

plugins {
    id("gradlebuild.distribution.api-java")
}

val implementationResources: Configuration by configurations.creating

repositories {
    googleApisJs()
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:file-collections"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))

    implementation(libs.groovy)
    implementation(libs.guava)
    implementation(libs.inject)
    implementation(libs.jatl)

    implementationResources("jquery:jquery.min:3.5.1@js")

    testImplementation(project(":distribution-core:process-services"))
    testImplementation(project(":distribution-core:base-services-groovy"))
    testImplementation(libs.jsoup)
    testImplementation(testFixtures(project(":distribution-core:core")))

    testRuntimeOnly(project(":distribution-setup:distributions-core")) {
        because("ProjectBuilder tests load services from a Gradle distribution.")
    }
    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-jvm")) {
        because("BuildDashboard has specific support for JVM plugins (CodeNarc, JaCoCo)")
    }
}

strictCompile {
    ignoreRawTypes() // raw types used in public API
    ignoreParameterizedVarargType() // [unchecked] Possible heap pollution from parameterized vararg type: GenerateBuildDashboard.aggregate()
}

classycle {
    excludePatterns.set(listOf("org/gradle/api/reporting/internal/**"))
}

val reportResources = tasks.register<Copy>("reportResources") {
    from(implementationResources)
    into(layout.buildDirectory.file("generated-resources/report-resources/org/gradle/reporting"))
}

sourceSets.main {
    output.dir(reportResources.map { it.destinationDir.parentFile.parentFile.parentFile })
}
