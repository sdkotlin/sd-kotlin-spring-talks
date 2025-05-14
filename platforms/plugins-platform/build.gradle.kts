plugins {
	id("java-platform")
}

dependencies {
	constraints {
		api(libs.jvmDependencyConflictResolution.gradlePluginDependency)
		api(libs.kotlin.gradlePluginDependency)
		api(libs.kotlin.spring.gradlePluginDependency)
		api(libs.osdetector.gradlePluginDependency)
		api(libs.springboot.gradlePluginDependency)
	}
}
