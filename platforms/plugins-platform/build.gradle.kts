plugins {
	id("java-platform")
}

dependencies {
	constraints {
		api(libs.jvm.dependency.conflict.resolution.gradle.plugin.dependency)
		api(libs.kotlin.gradle.plugin.dependency)
		api(libs.kotlin.spring.gradle.plugin.dependency)
		api(libs.osdetector.gradle.plugin.dependency)
		api(libs.springboot.gradle.plugin.dependency)
	}
}
