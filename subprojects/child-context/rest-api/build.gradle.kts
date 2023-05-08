plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(project(":subprojects:child-context:domain-service"))

	api(libs.log4j.api.kotlin)
	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.webflux)
	api(libs.spring.web)

	implementation(libs.spring.context)

	runtimeOnly(libs.bundles.kotlinx.coroutines.reactor)

	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.mockk)
}
