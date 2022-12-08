plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(libs.kotlinx.datetime.jvm)
	api(libs.spring.context)

	implementation(libs.bundles.kotlinx.coroutines.jvm)
}
