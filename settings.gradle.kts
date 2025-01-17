import de.fayard.refreshVersions.core.StabilityLevel
rootProject.name = "PhotosNetwork"

plugins {
    id("de.fayard.refreshVersions") version "0.50.1"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

include(":app")
include(":domain")
include(":data")
