pluginManagement {
	@Suppress("UnstableApiUsage")
	includeBuild("build-logic")
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
	}
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "sd-kotlin-spring-talks"

includeBuild("platforms")

include("subprojects:app")
include("subprojects:time-logger")
include("subprojects:time-service")
