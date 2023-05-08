plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.bundles.kotlinx.datetime.jvm)
	api(libs.log4j.api.kotlin)
	api(libs.spring.context)

	api(project(":subprojects:time-service"))

	integrationTestImplementation(libs.spring.boot.starter.test)
}
