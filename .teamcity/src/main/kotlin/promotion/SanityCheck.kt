package promotion

import common.Os
import common.gradleWrapper
import common.requiresOs
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import vcsroots.gradlePromotionMaster
import vcsroots.useAbsoluteVcs

// GradleBuildTool_Master_Promotion_SanityCheck
object SanityCheck : BuildType({
    // From Gradle_Promotion_MasterSanityCheck
    id("Promotion_SanityCheck")
    name = "SanityCheck"
    description = "Sanity check for promotion project"

    vcs.useAbsoluteVcs(gradlePromotionMaster)

    steps {
        gradleWrapper {
            tasks = "tasks"
            gradleParams = ""
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
    }

    triggers {
        vcs {
            branchFilter = ""
        }
    }

    requirements {
        requiresOs(Os.LINUX)
    }
})
