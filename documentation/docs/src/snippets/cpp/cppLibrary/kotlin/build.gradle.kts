// tag::apply-plugin[]
plugins {
    `cpp-library`
}
// end::apply-plugin[]

// tag::distribution-plugins:core:dependency-management"[]
library {
    dependencies {
        api("io.qt:distribution-core:core:5.1")
        implementation("io.qt:network:5.1")
    }
}
// end::distribution-plugins:core:dependency-management"[]

// tag::configure-target-machines[]
library {
    targetMachines.set(listOf(machines.linux.x86_64,
        machines.windows.x86, machines.windows.x86_64,
        machines.macOS.x86_64))
}
// end::configure-target-machines[]

// tag::configure-linkages[]
library {
    linkage.set(listOf(Linkage.STATIC, Linkage.SHARED))
}
// end::configure-linkages[]
