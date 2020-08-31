plugins {
    id("gradlebuild.distribution.packaging")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-core")))

    pluginsRuntimeOnly(project(":distribution-plugins:basics:resources-http"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:resources-sftp"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:resources-s3"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:resources-gcs"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:resources-http"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:build-cache-http"))

    pluginsRuntimeOnly(project(":distribution-plugins:basics:tooling-api-builders"))
    pluginsRuntimeOnly(project(":distribution-plugins:basics:kotlin-dsl-tooling-builders"))

    pluginsRuntimeOnly(project(":distribution-plugins:basics:test-kit"))
}
