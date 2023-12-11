plugins {
	alias(libs.plugins.spring.boot.plugin)
	id("org.sdkotlin.buildlogic.spring-project")
}

dependencies {

	api(libs.spring.boot.starter)

	implementation(projects.subprojects.timeLogger)
	implementation(projects.subprojects.timeService)

	implementation(libs.bundles.kotlinx.coroutines.jvm)
	implementation(libs.log4j.api.kotlin)
	implementation(libs.spring.beans)
	implementation(libs.spring.boot)
	implementation(libs.spring.boot.autoconfigure)
	implementation(libs.spring.core)
	implementation(libs.spring.context)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.spring.boot.test)
	integrationTestImplementation(libs.spring.boot.starter.test)
}

springBoot {
	mainClass = "org.sdkotlin.springdemo.SpringBootAppKt"
}
