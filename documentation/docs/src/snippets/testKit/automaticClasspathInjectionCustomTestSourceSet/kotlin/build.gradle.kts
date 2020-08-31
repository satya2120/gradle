// tag::custom-test-source-set[]
plugins {
    groovy
    `java-gradle-plugin`
}

sourceSets {
    create("functionalTest") {
        withConvention(GroovySourceSet::class) {
            groovy {
                srcDir(file("src/functionalTest/groovy"))
            }
        }
        :distribution-core:resources {
            srcDir(file("src/functionalTest/:distribution-core:resources"))
        }
        compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath
        runtimeClasspath += output + compileClasspath
    }
}

tasks.register<Test>("functionalTest") {
    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
}

tasks.check { dependsOn(tasks["functionalTest"]) }

gradlePlugin {
    testSourceSets(sourceSets["functionalTest"])
}

dependencies {
    "functionalTestImplementation"("org.spockframework:spock-core:1.3-groovy-2.4") {
        exclude(module = "groovy-all")
    }
}
// end::custom-test-source-set[]

repositories {
    mavenCentral()
}
