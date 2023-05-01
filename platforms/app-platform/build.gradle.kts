plugins {
	id("java-platform")
}

group = "org.sdkotlin.platforms"

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.jackson.bom))
	api(platform(libs.kotlinx.coroutines.bom))
	api(platform(libs.log4j.bom))
	api(platform(libs.spring.boot.bom))

	constraints {
		api(libs.jackson.module.kogera)
		api(libs.jetbrains.annotations)
		api(libs.bundles.kotlinx.datetime.jvm)
		api(libs.bundles.log4j.kotlin)
	}
}
