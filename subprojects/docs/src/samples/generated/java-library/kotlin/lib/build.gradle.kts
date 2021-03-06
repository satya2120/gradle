
plugins {
    `java-library` // <1>
}

repositories {
    jcenter() // <2>
}

dependencies {
    api("org.apache.commons:commons-math3:3.6.1") // <3>

    implementation("com.google.guava:guava:29.0-jre") // <4>

    testImplementation("junit:junit:4.13") // <5>
}
