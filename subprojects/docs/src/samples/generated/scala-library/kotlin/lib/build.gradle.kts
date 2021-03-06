
plugins {
    scala // <1>

    `java-library` // <2>
}

repositories {
    jcenter() // <3>
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.3") // <4>

    testImplementation("junit:junit:4.12") // <5>
    testImplementation("org.scalatest:scalatest_2.13:3.2.0")
    testImplementation("org.scalatestplus:junit-4-12_2.13:3.2.0.0")

    testRuntimeOnly("org.scala-lang.modules:scala-xml_2.13:1.2.0") // <6>
}
