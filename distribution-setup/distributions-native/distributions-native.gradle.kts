plugins {
    id("gradlebuild.distribution.packaging")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-jvm"))) {
        because("the project dependency 'toolingNative -> ide' currently links this to the JVM ecosystem")
    }
    pluginsRuntimeOnly(platform(project(":distribution-setup:distributions-publishing"))) {
        because("configuring publishing is part of the 'language :distribution-core:native' support")
    }

    pluginsRuntimeOnly(project(":distribution-plugins:native:language-native"))
    pluginsRuntimeOnly(project(":distribution-plugins:native:tooling-native"))
    pluginsRuntimeOnly(project(":distribution-plugins:native:ide-native"))
    pluginsRuntimeOnly(project(":distribution-plugins:native:testing-native"))
}
