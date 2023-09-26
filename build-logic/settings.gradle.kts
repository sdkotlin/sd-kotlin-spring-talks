plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		gradlePluginPortal()
		maven { setUrl("https://repo.spring.io/milestone") }
	}
	versionCatalogs {
		create("libs") {
			from(files("../gradle/libs.versions.toml"))
		}
	}
}

includeBuild("../platforms")

rootProject.name = "build-logic"
