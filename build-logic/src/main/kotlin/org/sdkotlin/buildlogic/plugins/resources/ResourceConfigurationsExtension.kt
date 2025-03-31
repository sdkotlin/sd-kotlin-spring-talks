package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.sdkotlin.buildlogic.artifacts.dsl.AttributesDependencyCreationExtension
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.applyAttributes
import javax.inject.Inject

/**
 * A project extension to manage resource configurations. Provides the ability
 * to define and manage multiple resource configurations within a Gradle
 * project. Resource configurations represent collections of resource files
 * and their attributes, enabling streamlined sharing and consumption by other
 * projects.
 *
 * @constructor
 * @param project the associated Gradle project instance.
 * @see ResourceConfigurationsPlugin
 * @see ResourceConfiguration
 */
abstract class ResourceConfigurationsExtension @Inject constructor(
	private val project: Project,
) {
	private val resourceConfigurations: NamedDomainObjectSet<ResourceConfiguration> =
		project.objects.namedDomainObjectSet(ResourceConfiguration::class.java)

	/**
	 * Creates a new resource configuration and eagerly applies the given
	 * configuration action to it.
	 *
	 * @param name The name of the resource configuration to create.
	 * @param action A lambda for configuring the new `ResourceConfiguration`.
	 * Defaults to an empty configuration.
	 */
	fun create(name: String, action: ResourceConfiguration.() -> Unit = {}) {

		val resourceConfiguration =
			project.objects.newInstance(ResourceConfiguration::class.java, name)

		resourceConfiguration.action()

		with(resourceConfiguration) {

			val theConsumableConfigurationName =
				consumableConfigurationName.get()
			val theResourceDirectory = resourceDirectory.get()

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(theConsumableConfigurationName)
				.configure {
					attributes {
						applyAttributes(resourceAttributes)
					}
				}

			// Any files in "src/main/<resourceConfigurationName>/" are
			// resources for this configuration. No build step is necessary, so
			// directly add the directory as a project artifact.
			if (theResourceDirectory.asFile.exists()) {
				project.logger.lifecycle("Adding resource directory: {}",
					theResourceDirectory)
				project.artifacts.add(
					theConsumableConfigurationName,
					theResourceDirectory
				) {
					type = JVM_RESOURCES_DIRECTORY
				}
			}

			// Add a `DependencyHandler` extension for declaring a dependency on
			// the resource configuration's artifact variant.
			project.dependencies.extensions.add(
				DependencyCreationExtension::class.java,
				dependencyHandlerExtensionName.get(),
				AttributesDependencyCreationExtension(
					project.dependencies,
					resourceAttributes,
				)
			)
		}

		resourceConfigurations += resourceConfiguration
	}

	/**
	 * Retrieves the resource configuration with the specified name.
	 *
	 * @param name the name of the resource configuration to retrieve.
	 * @return the corresponding resource configuration.
	 */
	operator fun get(name: String): ResourceConfiguration =
		resourceConfigurations.getByName(name)
}
