plugins {
    id("gradlebuild.distribution.implementation-java")
}

dependencies {
    implementation(project(":distribution-core:launcher"))
    implementation(project(":distribution-core:base-services"))
    implementation(project(":distribution-core:messaging"))
    implementation(project(":distribution-core:native"))
    implementation(project(":distribution-core:logging"))
    implementation(project(":distribution-core:process-services"))
    implementation(project(":distribution-core:core-api"))
    implementation(project(":distribution-core:model-core"))
    implementation(project(":distribution-core:core"))
    implementation(project(":distribution-core:base-services-groovy")) // for 'Specs'
    implementation(project(":distribution-plugins:core:testing-base"))
    implementation(project(":distribution-plugins:core:testing-jvm"))
    implementation(project(":distribution-plugins:core:dependency-management"))
    implementation(project(":distribution-plugins:core:reporting"))
    implementation(project(":distribution-plugins:core:workers"))
    implementation(project(":distribution-plugins:core:composite-builds"))
    implementation(project(":distribution-core:tooling-api"))
    implementation(project(":distribution-core:build-events"))

    implementation(libs.groovy) // for 'Closure'
    implementation(libs.guava)
    implementation(libs.commonsIo)

    testImplementation(project(":distribution-core:file-collections"))
    testImplementation(project(":distribution-plugins:core:platform-jvm"))
}

strictCompile {
    ignoreDeprecations()
}
