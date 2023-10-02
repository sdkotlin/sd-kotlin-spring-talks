plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	// Include jackson-module-kogera instead of jackson-module-kotlin
	integrationTestImplementation(libs.jackson.module.kogera)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.starter.json)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test)
	integrationTestImplementation(libs.spring.boot.test.autoconfigure)
	integrationTestImplementation(libs.spring.context)
}
