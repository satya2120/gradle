// tag::repository-filter[]
repositories {
    maven {
        url = uri("https://repo.mycompany.com/maven2")
        content {
            // this repository *only* contains artifacts with group "my.company"
            includeGroup("my.company")
        }
    }
    jcenter {
        content {
            // this repository contains everything BUT artifacts with group starting with "my.company"
            excludeGroupByRegex("my\\.company.*")
        }
    }
}
// end::repository-filter[]

// tag::exclusive-repository-filter[]
repositories {
    // This repository will _not_ be searched for artifacts in my.company
    // despite being declared first
    jcenter()
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://repo.mycompany.com/maven2")
            }
        }
        filter {
            // this repository *only* contains artifacts with group "my.company"
            includeGroup("my.company")
        }
    }
}
// end::exclusive-repository-filter[]

// tag::repository-:distribution-core:snapshots[]
repositories {
    maven {
        url = uri("https://repo.mycompany.com/releases")
        mavenContent {
            releasesOnly()
        }
    }
    maven {
        url = uri("https://repo.mycompany.com/:distribution-core:snapshots")
        mavenContent {
            :distribution-core:snapshotsOnly()
        }
    }
}
// end::repository-:distribution-core:snapshots[]

val libs by configurations.creating

dependencies {
    libs("com.google.guava:guava:23.0")
}

tasks.register<Copy>("copyLibs") {
    from(libs)
    into("$buildDir/libs")
}
