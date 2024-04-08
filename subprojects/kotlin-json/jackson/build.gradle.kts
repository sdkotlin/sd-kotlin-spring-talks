plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.springboot.starter.json)
	integrationTestImplementation(libs.springboot.starter.test)
	integrationTestImplementation(libs.springboot.test)
	integrationTestImplementation(libs.springboot.test.autoconfigure)

	integrationTestRuntimeOnly(libs.jackson.module.kotlin)
}
