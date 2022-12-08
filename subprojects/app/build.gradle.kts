// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.spring.boot.plugin)
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(platform("org.sdkotlin.platforms:app-platform"))

	api(project(":subprojects:time-logger"))
	api(project(":subprojects:time-service"))

	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.webflux)
	api(libs.spring.context)

	implementation(libs.bundles.kotlinx.coroutines.jvm)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
