plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
	// Produces the partial report that the root project's
	// `dependencyUpdatesAggregation` entry merges.
	id("io.github.ben-manes.versions.settings") version "0.64.0"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		gradlePluginPortal()
	}
	versionCatalogs {
		create("libs") {
			from(files("../gradle/libs.versions.toml"))
		}
	}
}

rootProject.name = "build-logic"

includeBuild("../platforms")
