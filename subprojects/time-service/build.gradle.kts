plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	`java-test-fixtures`
}

dependencies {

	api(libs.kotlinx.datetime.jvm)
	api(libs.spring.context)

	implementation(libs.bundles.kotlinx.coroutines.jvm)

	testImplementation(project(":subprojects:time-service"))

	testImplementation(libs.bundles.kotlinx.coroutines.test.jvm)

	integrationTestImplementation(libs.kotlinx.coroutines.core)
	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.boot.starter.test)
	integrationTestImplementation(libs.spring.boot.test)
}
