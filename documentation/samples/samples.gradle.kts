import gradlebuild.cleanup.WhenNotEmpty
import gradlebuild.integrationtests.integrationTestUsesSampleDir

plugins {
    id("gradlebuild.internal.java")
}

dependencies {
    integTestImplementation(project(":distribution-core:base-services"))
    integTestImplementation(project(":distribution-core:core-api"))
    integTestImplementation(project(":distribution-core:process-services"))
    integTestImplementation(project(":distribution-core:persistent-cache"))
    integTestImplementation(libs.groovy)
    integTestImplementation(libs.slf4jApi)
    integTestImplementation(libs.guava)
    integTestImplementation(libs.ant)
    integTestImplementation(libs.sampleCheck) {
        exclude(group = "org.codehaus.groovy", module = "groovy-all")
        exclude(module = "slf4j-simple")
    }
    integTestImplementation(testFixtures(project(":distribution-core:core")))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}

integrationTestUsesSampleDir("subprojects/core-api/src/main/java", "subprojects/core/src/main/java")
