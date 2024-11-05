package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.kotlin.dsl.container
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributeDependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes

class ResourceConfigurationsPlugin : Plugin<Project> {

	companion object {
		const val RESOURCE_CONFIGURATION_EXTENSION_NAME =
			"resourceConfiguration"
	}

	override fun apply(project: Project) {

		val resourceConfigurationContainer: NamedDomainObjectContainer<ResourceConfiguration> =
			project.container<ResourceConfiguration>()

		project.extensions.add(
			RESOURCE_CONFIGURATION_EXTENSION_NAME,
			resourceConfigurationContainer
		)

		resourceConfigurationContainer.configureEach {

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			val consumableConfigurationName = "${name}Elements"
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(consumableConfigurationName) {
				attributes {
					applyLibraryElementsAttributes(
						project.objects,
						libraryElementsAttributeValue.get(),
					)
				}
			}

			// Add a `DependencyHandler` extension for declaring a dependency on
			// the resource configuration's artifact variant.
			val dependencyHandlerExtensionName = "${name}Resources"
			project.dependencies.extensions.add(
				DependencyCreationExtension::class.java,
				dependencyHandlerExtensionName,
				LibraryElementsAttributeDependencyCreationExtension(
					project.dependencies,
					project.objects,
					libraryElementsAttributeValue.get()
				)
			)

			// Any files in "src/main/<resourceConfiguration>/" are resources
			// for this configuration. No build step is necessary, so directly
			// add the directory as an artifact to the variant-aware consumable
			// configuration (if it exists).
			if (resourceDirectory.get().asFile.exists()) {

				project.artifacts.add(
					consumableConfigurationName,
					resourceDirectory
				) {
					type = JVM_RESOURCES_DIRECTORY
				}
			}
		}
	}
}
