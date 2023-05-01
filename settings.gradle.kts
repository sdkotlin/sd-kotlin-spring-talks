pluginManagement {
	includeBuild("build-logic")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
		maven { setUrl("https://jitpack.io") }
	}
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "sd-kotlin-spring-talks"

includeBuild("platforms")

include("subprojects:app")
include("subprojects:child-context:domain-service")
include("subprojects:child-context:rest-api")
include("subprojects:kotlin-json:jackson")
include("subprojects:kotlin-json:kogera")
include("subprojects:time-logger")
include("subprojects:time-service")
