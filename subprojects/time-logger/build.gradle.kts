plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(projects.subprojects.timeService)

	api(libs.bundles.kotlinx.datetime.jvm)
	api(libs.log4j.api.kotlin)
	api(libs.spring.context)

	testImplementation(libs.bundles.mockk.jvm)

	integrationTestImplementation(projects.subprojects.timeService)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.springboot.test)
}
