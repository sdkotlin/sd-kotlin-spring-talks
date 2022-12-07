plugins {
	id("java-platform")
}

group = "org.sdkotlin.platforms"

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.spring.boot.bom))

	constraints {
		api(libs.bundles.kotlinx.coroutines.jvm)
		api(libs.jetbrains.annotations)
		api(libs.slf4j.simple)
	}
}
