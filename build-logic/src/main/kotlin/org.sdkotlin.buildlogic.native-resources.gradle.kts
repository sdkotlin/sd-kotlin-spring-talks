import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.attributes.NativeAttributes.applyNativeAttributes
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfiguration
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsPlugin
import org.sdkotlin.buildlogic.tasks.PrintClasspath

plugins {
	id("com.google.osdetector")
	// Java plugin application only strictly needed for ensuring
	// `runtimeClasspath` exists for demo print tasks below.
	id("java")
}

val currentOsResourceConfigurationName = "native"

apply<ResourceConfigurationsPlugin>()

configure<NamedDomainObjectContainer<ResourceConfiguration>> {
	create(currentOsResourceConfigurationName) {
		attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue
			)
			applyNativeAttributes(
				objects,
				provider { osdetector.os }
			)
		}
	}
}

// Print tasks for demonstration purposes only...

val currentOsResourceConfiguration: ResourceConfiguration =
	the<NamedDomainObjectContainer<ResourceConfiguration>>()[currentOsResourceConfigurationName]

tasks {

	register<PrintClasspath>("printCurrentOsRuntimeClasspath") {

		group = "native-resources"
		description = "Prints the current OS runtime classpath"

		classpathName = "runtimeClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get()
		}
	}

	register<PrintClasspath>("printCurrentOsResourcesClasspath") {

		group = "native-resources"
		description =
			"Prints the current OS resources subset of the runtime classpath"

		classpathName = "currentOsResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							currentOsResourceConfiguration.libraryElementsAttributeValue
						)
						applyNativeAttributes(
							objects,
							provider { osdetector.os }
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithoutCurrentOsResources") {

		group = "native-resources"
		description =
			"Prints the runtime classpath without the custom resources"

		classpathName = "runtimeClasspathWithoutCurrentOsResources"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue =
								provider { CLASSES_AND_RESOURCES }
						)
					}
				}.files
		}
	}
}
