plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(project(":subprojects:time-service"))

	api(libs.bundles.kotlinx.datetime.jvm)
	api(libs.log4j.api.kotlin)
	api(libs.spring.context)

	testImplementation(libs.bundles.mockk.jvm)

	integrationTestImplementation(project(":subprojects:time-service"))

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test)
}
