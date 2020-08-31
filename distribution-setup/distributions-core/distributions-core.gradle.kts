plugins {
    id("gradlebuild.distribution.packaging")
}

dependencies {
    coreRuntimeOnly(platform(project(":distribution-setup:core-platform")))

    pluginsRuntimeOnly(project(":distribution-plugins:core:plugin-use")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:dependency-management")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:workers")) {
        because("This is a core extension module (see DynamicModulesClassPathProvider.GRADLE_EXTENSION_MODULES)")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:composite-builds")) {
        because("We always need a BuildStateRegistry service implementation for certain code in ':distribution-core:core' to work.")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:basics:tooling-api-builders")) {
        because("We always need a BuildEventListenerFactory service implementation for ':distribution-core:launcher' to create global services.")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:version-control")) {
        because("We always need a VcsMappingsStore service implementation to create 'ConfigurationContainer' in ':distribution-plugins:core:dependency-management'.")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:configuration-cache")) {
        because("We always need a BuildLogicTransformStrategy service implementation.")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:testing-junit-platform")) {
        because("All test workers have JUnit platform on their classpath (see ForkingTestClassProcessor.getTestWorkerImplementationClasspath).")
    }
    pluginsRuntimeOnly(project(":distribution-plugins:core:kotlin-dsl-provider-plugins")) {
        because("We need a KotlinScriptBasePluginsApplicator service implementation to use Kotlin DSL scripts.")
    }
}
