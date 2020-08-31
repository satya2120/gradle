plugins {
    id("gradlebuild.distribution.api-java")
}

gradlebuildJava.usedInWorkers()

dependencies {
    implementation(project(":distribution-core:cli"))

    implementation(project(":distribution-core:base-annotations"))
    implementation(libs.commonsLang)
}
