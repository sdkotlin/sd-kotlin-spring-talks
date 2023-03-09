// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.spring.boot.plugin)
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.boot.starter)

	implementation(project(":subprojects:time-logger"))
	implementation(project(":subprojects:time-service"))

	implementation(libs.bundles.kotlinx.coroutines.jvm)
	implementation(libs.spring.boot)
	implementation(libs.spring.boot.autoconfigure)
	implementation(libs.spring.context)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
