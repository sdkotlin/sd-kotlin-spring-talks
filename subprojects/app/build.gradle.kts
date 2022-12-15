// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.spring.boot.plugin)
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(platform("org.sdkotlin.platforms:app-platform"))

	api(libs.spring.boot.starter)
	api(libs.spring.boot.starter.webflux)

	implementation(project(":subprojects:time-logger"))
	implementation(project(":subprojects:time-service"))

	implementation(libs.spring.boot)
	implementation(libs.spring.boot.autoconfigure)
	implementation(libs.spring.context)
	implementation(libs.bundles.kotlinx.coroutines.jvm)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
