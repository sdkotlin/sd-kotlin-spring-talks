import org.gradle.api.attributes.LibraryElements.JAR
import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE
import org.sdkotlin.buildlogic.attributes.CurrentOsAttributeDisambiguationRule
import org.sdkotlin.buildlogic.attributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.attributes.applyNativeAttributes
import org.sdkotlin.buildlogic.osdetector.osAsOperatingSystemFamilyAttributeValue
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurations
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurationsPlugin
import org.sdkotlin.buildlogic.tasks.PrintClasspath

plugins {
	id("com.google.osdetector")
	// Java plugin application only strictly needed for ensuring
	// `runtimeClasspath` exists for demo print tasks below.
	id("java")
}

val nativeResourceConfigurationName = "native"
val linuxVariantName = "linux"
val macosVariantName = "macos"
val windowsVariantName = "windows"

apply<ResourceConfigurationsPlugin>()

configure<ResourceConfigurations> {
	create(nativeResourceConfigurationName) {
		variants {
			variant(linuxVariantName) {
				attributes {
					applyNativeAttributes(
						objects,
						linuxVariantName
					)
				}
			}
			variant(macosVariantName) {
				attributes {
					applyNativeAttributes(
						objects,
						macosVariantName
					)
				}
			}
			variant(windowsVariantName) {
				attributes {
					applyNativeAttributes(
						objects,
						windowsVariantName
					)
				}
			}
		}
	}
}

val currentOsAttributeValue =
	osdetector.osAsOperatingSystemFamilyAttributeValue()

dependencies {
	attributesSchema {
		attribute(OPERATING_SYSTEM_ATTRIBUTE) {
			disambiguationRules.add(
				CurrentOsAttributeDisambiguationRule::class.java
			) {
				params(
					objects.named<OperatingSystemFamily>(currentOsAttributeValue)
				)
			}
		}
	}
}

val nativeResourceConfiguration =
	the<ResourceConfigurations>()[nativeResourceConfigurationName]

val currentOsConfigurationVariant =
	nativeResourceConfiguration.resourceConfigurationVariants[currentOsAttributeValue]

val linuxConfigurationVariant =
	nativeResourceConfiguration.resourceConfigurationVariants[linuxVariantName]

val macosConfigurationVariant =
	nativeResourceConfiguration.resourceConfigurationVariants[macosVariantName]

val windowsConfigurationVariant =
	nativeResourceConfiguration.resourceConfigurationVariants[windowsVariantName]


// Print tasks for demonstration purposes only...

tasks {

	register<PrintClasspath>("printNativeResourcesRuntimeClasspath") {

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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue = JAR
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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						with(currentOsConfigurationVariant) {
							applyVariantAttributes()
						}
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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						with(linuxConfigurationVariant) {
							applyVariantAttributes()
						}
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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						with(macosConfigurationVariant) {
							applyVariantAttributes()
						}
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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						with(windowsConfigurationVariant) {
							applyVariantAttributes()
						}
					}
				}.files
		}
	}
}
