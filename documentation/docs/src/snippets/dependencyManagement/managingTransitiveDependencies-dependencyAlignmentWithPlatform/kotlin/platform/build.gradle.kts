// tag::platform[]
plugins {
    `java-platform`
}

dependencies {
    // The platform declares constraints on all components that
    // require alignment
    constraints {
        api(project(":distribution-core:core"))
        api(project(":lib"))
        api(project(":utils"))
    }
}
// end::platform[]


publishing {
    publications {
        create("maven", MavenPublication::class.java) {
            from(components["javaPlatform"])
        }
    }
}
