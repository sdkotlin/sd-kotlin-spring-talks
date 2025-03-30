import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfiguration
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsExtension
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsPlugin
import org.sdkotlin.buildlogic.tasks.PrintClasspath

plugins {
	// Java plugin application only strictly needed for ensuring
	// `runtimeClasspath` exists for demo print tasks below.
	id("java")
}

// The name for the "custom" resource configuration dependency variant.
val resourceConfigurationName = "custom"

apply<ResourceConfigurationsPlugin>()

configure<ResourceConfigurationsExtension> {
	create(resourceConfigurationName)
}

// Print tasks for demonstration purposes only...

val resourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[resourceConfigurationName]

tasks {

	register<PrintClasspath>("printRuntimeClasspath") {

		group = "custom-resources"
		description =
			"Prints the runtime classpath, including the custom resources."

		classpathName = "runtimeClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get()
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithOnlyCustomResources") {

		group = "custom-resources"
		description =
			"Prints only the custom resources from the runtime classpath."

		classpathName = "customResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							resourceConfiguration.libraryElementsAttributeValue.get()
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithoutCustomResources") {

		group = "custom-resources"
		description =
			"Prints the runtime classpath without the custom resources."

		classpathName = "runtimeClasspathWithoutCustomResources"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue =
								CLASSES_AND_RESOURCES
						)
					}
				}.files
		}
	}
}
