plugins {
	id("java-platform")
}

group = "org.sdkotlin.platforms"

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.kotlinx.coroutines.bom))
	api(platform(libs.spring.boot.bom))

	constraints {
		api(libs.jetbrains.annotations)
		api(libs.bundles.kotlinx.datetime.jvm)
	}
}
