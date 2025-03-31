import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.attributes.NativeAttributes.applyNativeAttributes
import org.sdkotlin.buildlogic.attributes.applyAttributes
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
val linuxResourceConfigurationName = "linux"
val macosResourceConfigurationName = "macos"
val windowsResourceConfigurationName = "windows"

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
	create(linuxResourceConfigurationName) {
		attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue.get()
			)
			applyNativeAttributes(
				objects,
				linuxResourceConfigurationName
			)
		}
	}
	create(macosResourceConfigurationName) {
		attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue.get()
			)
			applyNativeAttributes(
				objects,
				macosResourceConfigurationName
			)
		}
	}
	create(windowsResourceConfigurationName) {
		attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue.get()
			)
			applyNativeAttributes(
				objects,
				windowsResourceConfigurationName
			)
		}
	}
}

// Print tasks for demonstration purposes only...

val currentOsResourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[currentOsResourceConfigurationName]

val linuxResourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[linuxResourceConfigurationName]

val macosResourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[macosResourceConfigurationName]

val windowsResourceConfiguration: ResourceConfiguration =
	the<ResourceConfigurationsExtension>()[windowsResourceConfigurationName]

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

	register<PrintClasspath>("printRuntimeClasspathWithoutNativeResources") {

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
						applyAttributes(
							currentOsResourceConfiguration.resourceAttributes
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithOnlyLinuxResources") {

		group = "native-resources"
		description =
			"Prints only the Linux resources from the runtime classpath."

		classpathName = "linuxResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyAttributes(
							linuxResourceConfiguration.resourceAttributes
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithOnlyMacOSResources") {

		group = "native-resources"
		description =
			"Prints only the MacOS resources from the runtime classpath."

		classpathName = "macosResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyAttributes(
							macosResourceConfiguration.resourceAttributes
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithOnlyWindowsResources") {

		group = "native-resources"
		description =
			"Prints only the Windows resources from the runtime classpath."

		classpathName = "windowsResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyAttributes(
							windowsResourceConfiguration.resourceAttributes
						)
					}
				}.files
		}
	}
}
