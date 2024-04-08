plugins {
	id("java-platform")
}

group = "org.sdkotlin.platforms"

dependencies {
	constraints {
		api(libs.kotlin.gradle.plugin.dependency)
		api(libs.kotlin.spring.gradle.plugin.dependency)
		api(libs.springboot.gradle.plugin.dependency)
	}
}
