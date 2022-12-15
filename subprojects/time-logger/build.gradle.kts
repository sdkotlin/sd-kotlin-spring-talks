plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.context)

	implementation(project(":subprojects:time-service"))

	implementation(libs.log4j.api)
	implementation(libs.bundles.kotlinx.datetime.jvm)

	integrationTestImplementation(project(":subprojects:time-service"))
}
