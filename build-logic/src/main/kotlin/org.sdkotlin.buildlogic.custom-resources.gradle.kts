import org.gradle.api.attributes.LibraryElements.JAR
import org.sdkotlin.buildlogic.attributes.applyLibraryElementsAttributes
import org.sdkotlin.buildlogic.plugins.resources.ResourceConfigurations
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

configure<ResourceConfigurations> {
	create(resourceConfigurationName)
}

// Print tasks for demonstration purposes only...

val resourceConfigurationVariant =
	the<ResourceConfigurations>()[resourceConfigurationName]
		.resourceConfigurationVariants[resourceConfigurationName]

tasks {

	register<PrintClasspath>("printCustomResourcesRuntimeClasspath") {

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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						with(resourceConfigurationVariant) {
							applyVariantAttributes()
						}
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
			configurations.named("runtimeClasspath").get()
				.incoming.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue =
								JAR
						)
					}
				}.files
		}
	}
}
