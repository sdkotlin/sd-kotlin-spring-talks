plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.log4j.api.kotlin)
	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.webflux)
	api(libs.spring.web)

	runtimeOnly(libs.bundles.kotlinx.coroutines.reactor)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
