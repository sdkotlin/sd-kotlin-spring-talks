plugins {
	id("java-platform")
}

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.jackson.bom))
	api(platform(libs.kotlinx.coroutines.bom))
	api(platform(libs.log4j.bom))
	api(platform(libs.spring.bom))
	api(platform(libs.springboot.bom))

	constraints {
		api(libs.jackson.module.kogera)
		api(libs.jetbrains.annotations)
		api(libs.bundles.kotlinx.datetime.jvm)
		api(libs.bundles.log4j.kotlin)
	}
}
