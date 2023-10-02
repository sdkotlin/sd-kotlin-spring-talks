plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.starter.json)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test)
	integrationTestImplementation(libs.spring.boot.test.autoconfigure)
	integrationTestImplementation(libs.spring.context)

	integrationTestRuntimeOnly(libs.jackson.module.kotlin)
}
