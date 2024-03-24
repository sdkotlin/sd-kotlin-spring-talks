pluginManagement {
	repositories {
		gradlePluginPortal()
	}

	includeBuild("build-logic")
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
		maven { setUrl("https://jitpack.io") }
	}
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "sd-kotlin-spring-talks"

includeBuild("platforms")

include("subprojects:app")
include("subprojects:child-context:domain-service")
include("subprojects:child-context:rest-api")
include("subprojects:component-scanned-service")
include("subprojects:kotlin-json:jackson")
include("subprojects:kotlin-json:kogera")
include("subprojects:time-logger")
include("subprojects:time-service")
