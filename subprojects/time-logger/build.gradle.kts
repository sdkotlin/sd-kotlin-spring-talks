plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(project(":subprojects:time-service"))

	integrationTestImplementation(project(":subprojects:time-service"))
}
