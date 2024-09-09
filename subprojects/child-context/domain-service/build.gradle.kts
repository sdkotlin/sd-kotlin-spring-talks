plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(libs.spring.context)
	api(libs.springboot)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.spring.test)
	integrationTestImplementation(libs.springboot.test)
}
