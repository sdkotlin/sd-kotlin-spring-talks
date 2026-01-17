plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	integrationTestImplementation(libs.jackson.module.kotlin)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.springboot.test)
	integrationTestImplementation(libs.springboot.test.autoconfigure)

	integrationTestRuntimeOnly(libs.springboot.starter.json)
	integrationTestRuntimeOnly(libs.springboot.starter.test)
}
