plugins {
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.context)

	implementation(libs.spring.boot.starter)

	integrationTestImplementation(libs.spring.boot.starter.test)
}
