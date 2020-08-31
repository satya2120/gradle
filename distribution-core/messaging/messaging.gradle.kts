plugins {
    id("gradlebuild.distribution.api-java")
}

gradlebuildJava.usedInWorkers()

dependencies {
    implementation(project(":distribution-core:base-services"))

    implementation(libs.fastutil)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.kryo)

    testImplementation(testFixtures(project(":distribution-core:core")))

    testFixturesImplementation(project(":distribution-core:base-services"))
    testFixturesImplementation(libs.slf4jApi)

    integTestDistributionRuntimeOnly(project(":distribution-setup:distributions-core"))
}
