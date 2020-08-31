import gradlebuild.cleanup.WhenNotEmpty

plugins {
    id("gradlebuild.internal.java")
}

dependencies {
    integTestImplementation(project(":distribution-core:base-services"))
    integTestImplementation(project(":distribution-core:native"))
    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(project(":distribution-core:process-services"))
    integTestImplementation(project(":distribution-core:core-api"))
    integTestImplementation(project(":distribution-core:resources"))
    integTestImplementation(project(":distribution-core:persistent-cache"))
    integTestImplementation(project(":distribution-plugins:core:dependency-management"))
    integTestImplementation(project(":distribution-core:bootstrap"))
    integTestImplementation(project(":distribution-core:launcher"))
    integTestImplementation(libs.groovy)
    integTestImplementation(libs.slf4jApi)
    integTestImplementation(libs.guava)
    integTestImplementation(libs.ant)
    integTestImplementation(libs.jsoup)
    integTestImplementation(libs.jetty)
    integTestImplementation(libs.sampleCheck) {
        exclude(group = "org.codehaus.groovy", module = "groovy-all")
        exclude(module = "slf4j-simple")
    }

    crossVersionTestImplementation(project(":distribution-core:base-services"))
    crossVersionTestImplementation(project(":distribution-core:core"))
    crossVersionTestImplementation(project(":distribution-plugins:core:plugins"))
    crossVersionTestImplementation(project(":distribution-plugins:core:platform-jvm"))
    crossVersionTestImplementation(project(":distribution-plugins:core:language-java"))
    crossVersionTestImplementation(project(":distribution-plugins:core:language-groovy"))
    crossVersionTestImplementation(project(":distribution-plugins:jvm:scala"))
    crossVersionTestImplementation(project(":distribution-plugins:jvm:ear"))
    crossVersionTestImplementation(project(":distribution-plugins:core:testing-jvm"))
    crossVersionTestImplementation(project(":distribution-plugins:jvm:ide"))
    crossVersionTestImplementation(project(":distribution-plugins:jvm:code-quality"))
    crossVersionTestImplementation(project(":distribution-plugins:publishing:signing"))

    integTestImplementation(testFixtures(project(":distribution-core:core")))
    integTestImplementation(testFixtures(project(":distribution-plugins:core:diagnostics")))
    integTestImplementation(testFixtures(project(":distribution-plugins:native:platform-native")))
    integTestImplementation(libs.jgit)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
    crossVersionTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
