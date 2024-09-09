pluginManagement {
	repositories {
		gradlePluginPortal()
	}

	includeBuild("build-logic")
}

plugins {
	id("com.gradle.develocity") version "3.18"
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

develocity {
	buildScan {
		publishing.onlyIf { !System.getenv("CI").isNullOrEmpty() }
		termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
		termsOfUseAgree.set("yes")
	}
}

rootProject.name = "sd-kotlin-spring-talks"

includeBuild("platforms")

include("subprojects:app")
include("subprojects:child-context:domain-service")
include("subprojects:child-context:rest-api")
include("subprojects:component-scanned-service")
include("subprojects:custom-resources")
include("subprojects:custom-resources-intermediary")
include("subprojects:kotlin-json:jackson")
include("subprojects:kotlin-json:kogera")
include("subprojects:time-logger")
include("subprojects:time-service")
