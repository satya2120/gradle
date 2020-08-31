plugins {
    id("gradlebuild.distribution.api-java")
}

gradlebuildJava.usedForStartup()

dependencies {
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:logging"))
}
