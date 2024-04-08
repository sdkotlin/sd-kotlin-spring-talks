plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(projects.subprojects.childContext.domainService)

	api(libs.log4j.api.kotlin)
	api(libs.spring.web)
	api(libs.springboot.starter)
	api(libs.springboot.starter.webflux)

	implementation(libs.spring.context)

	runtimeOnly(libs.bundles.kotlinx.coroutines.reactor)

	integrationTestImplementation(projects.subprojects.childContext.domainService)

	integrationTestImplementation(libs.bundles.mockk.jvm)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.spring.mockk)
	integrationTestImplementation(libs.spring.test)
	integrationTestImplementation(libs.springboot.autoconfigure)
	integrationTestImplementation(libs.springboot.test.autoconfigure)
	integrationTestImplementation(libs.springboot.starter.test)
}
