plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.boot)
	api(libs.spring.context)

	implementation(libs.spring.boot.starter)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.spring.test)
}
