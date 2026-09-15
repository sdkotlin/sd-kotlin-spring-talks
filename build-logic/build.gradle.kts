import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
	// Not using `id("kotlin-dsl")` per:
	// https://github.com/gradle/gradle/issues/23884
	`kotlin-dsl`
	`java-gradle-plugin`
}

group = "org.sdkotlin.buildlogic"
version = "1.0.0-SNAPSHOT"

dependencies {

	implementation(platform("org.sdkotlin.platforms:plugins-platform"))

	implementation(libs.dependencyAnalysis.gradlePluginDependency)
	implementation(libs.jvmDependencyConflictResolution.gradlePluginDependency)
	implementation(libs.kotlin.gradlePluginDependency)
	implementation(libs.kotlin.spring.gradlePluginDependency)
	implementation(libs.osdetector.gradlePluginDependency)
	implementation(libs.springboot.gradlePluginDependency)
}

gradlePlugin {
	plugins {
		create("resourceConfigurations") {
			id = "org.sdkotlin.buildlogic.resource-configurations"
			implementationClass =
				"org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsPlugin"
		}
	}
}

tasks {
	// `checkConstraints` is read in the build that resolves, so it is declared
	// here as well as in the root build.
	named<DependencyUpdatesTask>("dependencyUpdates").configure {
		checkConstraints = true
	}
}
