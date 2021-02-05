package util

import common.Os
import common.buildToolGradleParameters
import common.gradleWrapper
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.Requirement
import jetbrains.buildServer.configs.kotlin.v2019_2.RequirementType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import vcsroots.gradleMaster
import vcsroots.useAbsoluteVcs

object WarmupEc2Agent : BuildType({
    name = "Warmup EC2 Agent"
    id("Util_WarmupEc2Agent")

    vcs.useAbsoluteVcs(gradleMaster)

    features {
        freeDiskSpace {
            // Lower the limit such that the agent work directories aren't cleaned during the AMI baking process
            requiredSpace = "100mb"
        }
    }

    params {
        param("defaultBranchName", "master")
        param("env.JAVA_HOME", Os.LINUX.buildJavaHome())
    }

    steps {
        gradleWrapper {
            name = "Resolve all dependencies"
            tasks = "resolveAllDependencies"
            gradleParams = (
                buildToolGradleParameters(isContinue = false)
                ).joinToString(separator = " ")
        }
    }

    requirements {
        requirement(Requirement(RequirementType.EQUALS, "teamcity.agent.name", "ec2-agent1"))
    }
})
