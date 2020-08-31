plugins {
    id("gradlebuild.distribution.api-java")
}

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:resources"))
    implementation(project(":distribution-plugins:basics:resources-http"))
    implementation(project(":distribution-core:core"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsLang)
    implementation(libs.jacksonCore)
    implementation(libs.jacksonAnnotations)
    implementation(libs.jacksonDatabind)
    implementation(libs.gcs)
    implementation(libs.commonsHttpclient)
    implementation(libs.joda)

    testImplementation(libs.groovy)
    testImplementation(testFixtures(project(":distribution-core:core")))
    testImplementation(testFixtures(project(":distribution-plugins:core:dependency-management")))
    testImplementation(testFixtures(project(":distribution-plugins:core:ivy")))
    testImplementation(testFixtures(project(":distribution-plugins:core:maven")))

    integTestImplementation(project(":distribution-core:core-api"))
    integTestImplementation(project(":distribution-core:model-core"))
    integTestImplementation(libs.commonsIo)
    integTestImplementation(libs.jetty)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-basics"))
}
