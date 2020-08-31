// tag::declare-task[]
tasks.register<Copy>("myCopy")
// end::declare-task[]

// tag::configure[]
// Configure task using Kotlin delegated properties and a lambda
val myCopy by tasks.existing(Copy::class) {
    from(":distribution-core:resources")
    into("target")
}
myCopy { include("**/*.txt", "**/*.xml", "**/*.properties") }
// end::configure[]
