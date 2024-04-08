plugins {
	application
	alias(libs.plugins.springboot.plugin)
	id("org.sdkotlin.buildlogic.custom-resources-convention")
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

dependencies {

	api(libs.springboot.starter)

	implementation(projects.subprojects.timeLogger)
	implementation(projects.subprojects.timeService)
	implementation(projects.subprojects.componentScannedService)

	implementation(libs.bundles.kotlinx.coroutines.jvm)
	implementation(libs.log4j.api.kotlin)
	implementation(libs.spring.beans)
	implementation(libs.spring.core)
	implementation(libs.spring.context)
	implementation(libs.springboot)
	implementation(libs.springboot.autoconfigure)

	// A project with a transitive dependency on a project with a custom
	// resources artifact.
	customScope(projects.subprojects.customResources)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.springboot.test)
	integrationTestImplementation(libs.springboot.starter.test)
}

application {
	mainClass = "org.sdkotlin.springdemo.SpringBootAppKt"
}

springBoot {
	mainClass = "org.sdkotlin.springdemo.SpringBootAppKt"
}
