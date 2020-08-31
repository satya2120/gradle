plugins {
    id("gradlebuild.distribution.implementation-java")
}

gradlebuildJava.usedInWorkers()

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:process-services"))

    implementation(libs.slf4jApi)

    testImplementation(testFixtures(project(":distribution-core:core")))
}
