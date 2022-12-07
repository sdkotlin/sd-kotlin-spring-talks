plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(platform("org.sdkotlin.platforms:app-platform"))

	api(libs.spring.boot.starter)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
