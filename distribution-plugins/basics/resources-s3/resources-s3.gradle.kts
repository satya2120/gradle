import gradlebuild.cleanup.WhenNotEmpty

plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:basics:resources-http"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.nativePlatform)
    implementation(libs.awsS3Core)
    implementation(libs.awsS3S3)
    implementation(libs.awsS3Kms)
    implementation(libs.jaxb)
    implementation(libs.jacksonCore)
    implementation(libs.jacksonAnnotations)
    implementation(libs.jacksonDatabind)
    implementation(libs.commonsHttpclient)
    implementation(libs.joda)
    implementation(libs.commonsLang)

    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:core:ivy")))
    testImplementation(testFixtures(project(":distribution-plugins:core:maven")))

    integTestImplementation(project(":distribution-core:logging"))
    integTestImplementation(libs.commonsIo)
    integTestImplementation(libs.littleproxy)
    integTestImplementation(libs.jetty)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}

testFilesCleanup {
    policy.set(WhenNotEmpty.REPORT)
}
