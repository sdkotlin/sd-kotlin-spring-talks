plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	integrationTestImplementation(libs.jackson.module.kogera)
	integrationTestImplementation(libs.spring.boot.starter.json)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test.autoconfigure)
	integrationTestImplementation(libs.spring.context)
}
