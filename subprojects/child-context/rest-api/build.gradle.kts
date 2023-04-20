plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.log4j.api.kotlin)
	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.validation)
	api(libs.spring.boot.starter.webflux)
	api(libs.spring.web)

	implementation(project(":subprojects:child-context:domain-service"))

	runtimeOnly(libs.bundles.kotlinx.coroutines.reactor)

	integrationTestImplementation(project(":subprojects:child-context:domain-service"))

	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.mockk)
}
