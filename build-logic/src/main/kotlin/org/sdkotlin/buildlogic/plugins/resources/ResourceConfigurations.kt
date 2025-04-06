package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.sdkotlin.buildlogic.artifacts.dsl.AttributesDependencyCreationExtension
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import javax.inject.Inject

/**
 * A project extension for managing and configuring resource configurations
 * within a project, enabling variant-aware handling and dependency management
 * for resource configurations.
 *
 * This class operates on a named domain object set of `ResourceConfiguration`
 * instances and facilitates the addition of `DependencyHandler` extensions to
 * manage dependencies for individual resource configuration artifact variants.
 *
 * @constructor Creates a new instance of this extension and initializes the
 * resource configurations collection.
 * @param project the Gradle project associated with this extension.
 */
abstract class ResourceConfigurations @Inject constructor(
	private val project: Project,
) {
	private val resourceConfigurations: NamedDomainObjectSet<ResourceConfiguration> =
		project.objects.namedDomainObjectSet(ResourceConfiguration::class.java)

	/**
	 * Creates and configures a new resource configuration with the specified
	 * name. The created configuration can have its attributes and variants
	 * customized using the provided configuration block.
	 *
	 * Adds a `DependencyHandler` extension for managing dependencies on the
	 * resource configuration's artifact variant. If no variants are explicitly
	 * defined for the resource configuration, a default variant is created
	 * using the configuration's name.
	 *
	 * @param name the name of the resource configuration being created. This
	 * name is used to derive defaults for attributes and variants associated
	 * with the configuration.
	 * @param configureAction a configuration block to customize the resource
	 * configuration's attributes, dependencies, and variants. The block is
	 * executed on the created configuration instance.
	 */
	fun create(
		name: String,
		configureAction: ResourceConfiguration.() -> Unit = {},
	) {

		val resourceConfiguration =
			project.objects.newInstance(ResourceConfiguration::class.java, name)

		configureAction(resourceConfiguration)

		with(resourceConfiguration) {

			// Add a `DependencyHandler` extension for declaring a dependency on
			// the resource configuration's artifact variant.
			project.dependencies.extensions.add(
				DependencyCreationExtension::class.java,
				dependencyHandlerExtensionName.get(),
				AttributesDependencyCreationExtension(
					project.dependencies,
					configurationAttributesAction,
				)
			)

			// Create a default variant for this resource configuration if none
			// are added by the user.
			if (resourceConfigurationVariants.isEmpty()) {
				resourceConfigurationVariants.variant(name)
			}
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
