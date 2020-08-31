package configurations

import common.Os
import common.applyPerformanceTestSettings
import common.buildToolGradleParameters
import common.checkCleanM2
import common.gradleWrapper
import common.individualPerformanceTestArtifactRules
import common.killGradleProcessesStep
import common.performanceTestCommandLine
import common.removeWindowsSubstrStep
import common.windowsSubstrStep
import jetbrains.buildServer.configs.kotlin.v2019_2.AbsoluteId
import model.CIBuildModel

class IndividualPerformanceScenarioWorkers(model: CIBuildModel, os: Os = Os.LINUX) : BaseGradleBuildType(model, init = {
    uuid = model.projectPrefix + "IndividualPerformanceScenarioWorkers${os.capitalized()}"
    id = AbsoluteId(uuid)
    name = "Individual Performance Scenario Workers - ${os.capitalized()}"

    applyPerformanceTestSettings(os = os, timeout = 420)
    artifactRules = individualPerformanceTestArtifactRules

    params {
        param("baselines", "defaults")
        param("templates", "")
        param("channel", "commits")
        param("checks", "all")
        param("runs", "defaults")
        param("warmups", "defaults")
        param("scenario", "")

        param("env.ANDROID_HOME", os.androidHome)
        when (os) {
            Os.WINDOWS -> param("env.PATH", "%env.PATH%;C:/Program Files/7-zip")
            else -> param("env.PATH", "%env.PATH%:/opt/swift/4.2.3/usr/bin")
        }
    }

    steps {
        killGradleProcessesStep(os)
        windowsSubstrStep(os)
        gradleWrapper {
            name = "GRADLE_RUNNER"
            tasks = ""
            workingDir = "P:/"
            gradleParams = (
                performanceTestCommandLine(
                    "clean %templates% :performance:fullPerformanceTest",
                    "%baselines%",
                    """--scenarios "%scenario%" --warmups %warmups% --runs %runs% --checks %checks% --channel %channel%""",
                    os
                ) +
                    buildToolGradleParameters(isContinue = false, os = os) +
                    buildScanTag("IndividualPerformanceScenarioWorkers") +
                    model.parentBuildCache.gradleParameters(os)
                ).joinToString(separator = " ")
        }
        removeWindowsSubstrStep(os)
        checkCleanM2(os)
    }

    applyDefaultDependencies(model, this)
})
