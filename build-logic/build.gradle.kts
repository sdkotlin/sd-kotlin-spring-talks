plugins {
	// Not using `id("kotlin-dsl")` per:
	// https://github.com/gradle/gradle/issues/23884
	`kotlin-dsl`
	`java-gradle-plugin`
}

dependencies {

	implementation(platform("org.sdkotlin.platforms:plugins-platform"))

	implementation(libs.dependency.analysis.gradle.plugin.dependency)
	implementation(libs.jvm.dependency.conflict.resolution.gradle.plugin.dependency)
	implementation(libs.kotlin.gradle.plugin.dependency)
	implementation(libs.kotlin.spring.gradle.plugin.dependency)
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
