package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.file.Directory
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.kotlin.dsl.create
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributeDependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes

class ResourceConfigurationsPlugin : Plugin<Project> {

	override fun apply(project: Project) {

		project.pluginManager.apply(JavaBasePlugin::class.java)

		val extension = project.extensions
				.create<ResourceConfigurationsExtension>("resourceConfigs")

		extension.resourceConfigurations.get().forEach { resourceConfiguration ->

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			val consumableConfigurationName = "${resourceConfiguration}Elements"
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(consumableConfigurationName) {
				attributes {
					applyLibraryElementsAttributes(
						project.objects,
						"$resourceConfiguration-resources"
					)
				}
			}

			// Add a `DependencyHandler` extension for declaring a dependency on
			// the resource configuration's artifact variant.
			val dependencyHandlerExtensionName = "${resourceConfiguration}Resources"
			project.dependencies.extensions.add(
				DependencyCreationExtension::class.java,
				dependencyHandlerExtensionName,
				LibraryElementsAttributeDependencyCreationExtension(
					dependencyHandler = project.dependencies,
					objects = project.objects,
					libraryElementsAttributeValue =
						"$resourceConfiguration-resources"
				)
			)

			// Any files in "src/main/<resourceConfiguration>/" are resources
			// for this configuration.
			val resourcesDirectory: Directory =
				project.layout.projectDirectory
						.dir("src/main/$resourceConfiguration")

			// No build step is necessary, so directly add the directory as an
			// artifact to the variant-aware consumable configuration (if it
			// exists).
			if (resourcesDirectory.asFile.exists()) {
				project.artifacts.add(
					consumableConfigurationName,
					resourcesDirectory
				) {
					type = JVM_RESOURCES_DIRECTORY
				}
			}
		}
	}
}
