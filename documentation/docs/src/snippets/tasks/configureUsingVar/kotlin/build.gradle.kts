tasks.register<Copy>("myCopy")

// tag::configure[]
val myCopy = tasks.named<Copy>("myCopy")
myCopy {
    from(":distribution-core:resources")
    into("target")
    include("**/*.txt", "**/*.xml", "**/*.properties")
}
// end::configure[]
