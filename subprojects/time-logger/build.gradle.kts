plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.context)

	implementation(project(":subprojects:time-service"))

	implementation(libs.bundles.kotlinx.datetime.jvm)
	implementation(libs.log4j.api.kotlin)

	testImplementation(libs.log4j.api.kotlin)

	integrationTestImplementation(project(":subprojects:time-service"))

	integrationTestImplementation(libs.spring.boot.starter.test)
}
