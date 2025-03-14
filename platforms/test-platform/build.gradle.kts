plugins {
	id("java-platform")
}

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.junit.bom))
	api(platform(libs.kotlinx.coroutines.bom))

	constraints {
		api(libs.assertj)
		api(libs.bundles.mockk.jvm)
		api(libs.spring.mockk)
	}
}
