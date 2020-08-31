plugins {
    `java-library`
}

allprojects {
    repositories {
        jcenter()
    }
}

// tag::dependencies[]
dependencies {
    implementation(project(":lib"))
}
// end::dependencies[]

// tag::substitution_rule[]
configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(platform(module("com.google.guava:guava:28.2-jre")))
            .using(module("com.google.guava:guava:28.2-jre"))
    }
}
// end::substitution_rule[]

// tag::substitution_rule_alter:distribution-core:native[]
configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(variant(module("com.google.guava:guava:28.2-jre")) {
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.REGULAR_PLATFORM))
            }
        }).using(module("com.google.guava:guava:28.2-jre"))
    }
}
// end::substitution_rule_alter:distribution-core:native[]

tasks.register("resolve") {
    inputs.files(configurations.runtimeClasspath)
    doLast {
        println(configurations.runtimeClasspath.files.map { it.name })
    }
}
