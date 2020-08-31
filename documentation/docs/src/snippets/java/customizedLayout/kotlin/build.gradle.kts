plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

// tag::define-main[]
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/java"))
        }
        :distribution-core:resources {
            setSrcDirs(listOf("src/:distribution-core:resources"))
        }
    }
// end::define-main[]
    test {
        java {
            srcDir("test/java")
        }
        :distribution-core:resources {
            srcDir("test/:distribution-core:resources")
        }
    }
// tag::define-main[]
}
// end::define-main[]
