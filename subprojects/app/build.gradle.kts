import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCE
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.applyResourceAttributes

plugins {
	application
	alias(libs.plugins.springboot.plugin)
	id("org.sdkotlin.buildlogic.custom-resources-consumer")
	id("org.sdkotlin.buildlogic.spring-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
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

	integrationTestImplementation(libs.spring.beans)
	integrationTestImplementation(libs.spring.context)
	integrationTestImplementation(libs.spring.core)
	integrationTestImplementation(libs.springboot.test)
}

application {
	mainClass = "org.sdkotlin.springdemo.SpringBootAppKt"
}

tasks {

	register("printCustomClasspath") {

		val fileCollection: FileCollection =
			configurations.runtimeClasspath.get().incoming.artifactView {

				lenient(false)

				@Suppress("UnstableApiUsage")
				withVariantReselection()

				attributes {
					applyResourceAttributes(objects, CUSTOM_RESOURCE)
				}
			}.files

		doLast {

			val customResourcesAsPath = fileCollection.asPath

			println("customResourcesAsPath: $customResourcesAsPath")
		}
	}
}
