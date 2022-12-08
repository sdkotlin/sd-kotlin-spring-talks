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
		api(libs.kotlinx.datetime.jvm)
		api(libs.slf4j.simple)
	}
}
