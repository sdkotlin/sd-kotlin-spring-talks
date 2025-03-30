import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.attributes.NativeAttributes.applyNativeAttributes
import org.sdkotlin.buildlogic.osdetector.osAsOperatingSystemFamilyAttributeValue
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfiguration
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsExtension
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

configure<ResourceConfigurationsExtension> {
	create(currentOsResourceConfigurationName) {

		val operatingSystemAttributeValue =
			osdetector.osAsOperatingSystemFamilyAttributeValue()

		resourceDirectory = layout.projectDirectory
			.dir("src/main/$operatingSystemAttributeValue")

		attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue.get()
			)
			applyNativeAttributes(
				objects,
				operatingSystemAttributeValue
			)
		}
	}
}

// Print tasks for demonstration purposes only...

val currentOsResourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[currentOsResourceConfigurationName]

tasks {

	register<PrintClasspath>("printRuntimeClasspath") {

		group = "native-resources"
		description =
			"Prints the runtime classpath, including the native resources " +
				"for the current OS."

		classpathName = "runtimeClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get()
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithOnlyCurrentOsResources") {

		group = "native-resources"
		description =
			"Prints only the current OS resources from the runtime classpath."

		classpathName = "currentOsResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							currentOsResourceConfiguration.libraryElementsAttributeValue.get()
						)
						applyNativeAttributes(
							objects,
							osdetector.osAsOperatingSystemFamilyAttributeValue()
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithoutCurrentOsResources") {

		group = "native-resources"
		description =
			"Prints the runtime classpath without the current OS resources."

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
								CLASSES_AND_RESOURCES
						)
					}
				}.files
		}
	}
}
