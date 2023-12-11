plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(projects.subprojects.childContext.domainService)

	api(libs.log4j.api.kotlin)
	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.webflux)
	api(libs.spring.web)

	implementation(libs.spring.context)

	runtimeOnly(libs.bundles.kotlinx.coroutines.reactor)

	integrationTestImplementation(projects.subprojects.childContext.domainService)

	integrationTestImplementation(libs.bundles.mockk.jvm)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.autoconfigure)
	integrationTestImplementation(libs.spring.boot.test.autoconfigure)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.spring.mockk)
	integrationTestImplementation(libs.spring.test)
}
