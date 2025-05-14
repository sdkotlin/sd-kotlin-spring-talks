plugins {
	application
	alias(libs.plugins.springboot.gradlePlugin)
	id("org.sdkotlin.buildlogic.custom-resources")
	id("org.sdkotlin.buildlogic.native-resources")
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
}

application {
	mainClass = "org.sdkotlin.springdemo.SpringBootAppKt"
}

dependencies {

	implementation(projects.subprojects.timeLogger)
	implementation(projects.subprojects.timeService)
	implementation(projects.subprojects.componentScannedService)

	implementation(libs.bundles.kotlinx.coroutines.jvm)
	implementation(libs.log4j.api.kotlin)
	implementation(libs.spring.beans)
	implementation(libs.spring.context)
	implementation(libs.spring.core)
	implementation(libs.springboot)
	implementation(libs.springboot.autoconfigure)

	// A project with a transitive dependency on a project with a custom
	// resources artifact.
	runtimeOnly(projects.subprojects.customResourcesIntermediary)
	// A project with a transitive dependency on a project with a native
	// resources artifact.
	runtimeOnly(projects.subprojects.nativeResourcesIntermediary)

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.springboot.test)
}
