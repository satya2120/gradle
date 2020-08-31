plugins {
    id("org.gradle.sample.download")
}

download {
    // Can use a block to configure the container contents
    :distribution-core:resources {
        register("gradle") {
            uri = uri("https://gradle.org")
        }
    }
}
