plugins {
	`kotlin-dsl`
}

dependencies {

	implementation(platform("org.sdkotlin.platforms:plugins-platform"))

	implementation(libs.dependency.analysis.gradle.plugin.dependency)
	implementation(libs.kotlin.gradle.plugin.dependency)
	implementation(libs.kotlin.spring.gradle.plugin.dependency)
	implementation(libs.spring.boot.gradle.plugin.dependency)
}
