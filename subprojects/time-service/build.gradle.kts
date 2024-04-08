plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
	`java-test-fixtures`
}

dependencies {

	api(libs.kotlinx.datetime.jvm)
	api(libs.spring.context)

	implementation(libs.bundles.kotlinx.coroutines.jvm)

	testImplementation(projects.subprojects.timeService)

	testImplementation(libs.bundles.kotlinx.coroutines.test.jvm)

	integrationTestImplementation(libs.kotlinx.coroutines.core)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.springboot.starter.test)
	integrationTestImplementation(libs.springboot.test)
}
