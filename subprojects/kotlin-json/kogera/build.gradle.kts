plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	// Include jackson-module-kogera instead of jackson-module-kotlin
	integrationTestImplementation(libs.jackson.module.kogera)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.springboot.starter.json)
	integrationTestImplementation(libs.springboot.starter.test)
	integrationTestImplementation(libs.springboot.test)
	integrationTestImplementation(libs.springboot.test.autoconfigure)
}
