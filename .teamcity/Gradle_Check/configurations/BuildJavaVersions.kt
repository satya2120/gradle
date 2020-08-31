package configurations

import common.JvmVendor
import common.JvmVersion
import common.Os

fun buildJavaHome(os: Os = Os.LINUX) = "%${os.lowerCase()}.${JvmVersion.java11}.${JvmVendor.openjdk}.64bit%"

fun individualPerformanceTestJavaHome(os: Os) = "%${os.lowerCase()}.${JvmVersion.java8}.${JvmVendor.oracle}.64bit%"
