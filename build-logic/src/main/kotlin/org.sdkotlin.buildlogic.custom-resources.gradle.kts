import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfiguration
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsPlugin

plugins {
	// Java plugin application only strictly needed for ensuring
	// `runtimeClasspath` exists for demo print tasks below.
	id("java")
}

// The name for the "custom" resource configuration dependency variant.
val resourceConfigurationName = "custom"

apply<ResourceConfigurationsPlugin>()

configure<NamedDomainObjectContainer<ResourceConfiguration>> {
	create(resourceConfigurationName)
}

// Print tasks for demonstration purposes only...

val resourceConfiguration: ResourceConfiguration =
	the<NamedDomainObjectContainer<ResourceConfiguration>>()[resourceConfigurationName]

tasks {

	register<PrintClasspath>("printRuntimeClasspath") {

		group = "custom-resources"
		description = "Prints the runtime classpath"

		classpathName = "runtimeClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get()
		}
	}

	register<PrintClasspath>("printCustomResourcesClasspath") {

		group = "custom-resources"
		description =
			"Prints the custom resources subset of the runtime classpath"

		classpathName = "customResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							"${resourceConfiguration.name}-resources"
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithoutCustomResources") {

		group = "custom-resources"
		description =
			"Prints the runtime classpath without the custom resources"

		classpathName = "runtimeClasspathWithoutCustomResources"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue = CLASSES_AND_RESOURCES
						)
					}
				}.files
		}
	}
}

@CacheableTask
abstract class PrintClasspath : DefaultTask() {

	@get:Input
	abstract val classpathName: Property<String>

	@get:Classpath
	abstract val classpath: Property<FileCollection>

	@TaskAction
	fun printClasspath() {

		val classpathAsPath = classpath.get().asPath

		val wrappedClasspath =
			classpathAsPath.replace(File.pathSeparator, "\n")

		logger.lifecycle("${classpathName.get()}: \n$wrappedClasspath")
	}
}
