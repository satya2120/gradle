plugins {
    id("gradlebuild.internal.java")
}

dependencies {
    integTestImplementation(project(":fixtures:internal-testing"))
    integTestImplementation(project(":distribution-core:base-services"))
    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(project(":distribution-core:core-api"))
    integTestImplementation(libs.guava)
    integTestImplementation(libs.commonsIo)
    integTestImplementation(libs.ant)

    integTestBinDistribution(project(":distribution-setup:distributions-full"))
    integTestAllDistribution(project(":distribution-setup:distributions-full"))
    integTestDocsDistribution(project(":distribution-setup:distributions-full"))
    integTestSrcDistribution(project(":distribution-setup:distributions-full"))

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-full"))
}

tasks.forkingIntegTest.configure {
    systemProperty("gradleBuildBranch", moduleIdentity.gradleBuildBranch.get())
    systemProperty("gradleBuildCommitId", moduleIdentity.gradleBuildCommitId.get())
}
