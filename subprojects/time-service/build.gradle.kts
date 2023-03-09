plugins {
	id("org.sdkotlin.buildlogic.spring-project")
	`java-test-fixtures`
}

dependencies {

	api(libs.kotlinx.datetime.jvm)
	api(libs.spring.context)

	implementation(libs.bundles.kotlinx.coroutines.jvm)

	testImplementation(libs.bundles.kotlinx.coroutines.test.jvm)

	integrationTestImplementation(libs.bundles.kotlinx.coroutines.jvm)
	integrationTestImplementation(libs.spring.boot.starter.test)
}
