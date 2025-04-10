package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * A class responsible for managing and defining resource configuration
 * variants within a project. Each resource configuration variant represents a
 * specific set of attributes and associated resources. The class facilitates
 * the creation, customization, and retrieval of these configuration variants.
 *
 * Instances of this class are injected into a resource configuration and
 * operate within the context of a Gradle project.
 *
 * @constructor Creates an instance with the specified resource configuration,
 * project, and object factory.
 */
abstract class ResourceConfigurationVariants @Inject constructor(
	private val configurationAttributesConfigureAction: AttributeContainer.() -> Unit,
	private val project: Project,
	private val objects: ObjectFactory,
) {
	private val logger = LoggerFactory.getLogger(this::class.java)

	private val resourceConfigurationVariants:
		NamedDomainObjectSet<ResourceConfigurationVariant> =
		objects.namedDomainObjectSet(ResourceConfigurationVariant::class.java)

	/**
	 * Creates and configures a new resource configuration variant with the
	 * specified name. The variant represents a unique set of resource
	 * attributes and resource directory, allowing for variant-aware handling
	 * and consumption of resources by other projects. If the resource
	 * directory for the variant exists, it is added as a project artifact for
	 * consumption.
	 *
	 * @param name the name of the resource configuration variant being created.
	 * It is used to determine the default values for properties such as the
	 * resource directory and consumable configuration name.
	 * @param configureAction a configuration block to customize the resource
	 * configuration variant's attributes and other properties. The block is
	 * executed on the created variant instance.
	 */
	fun variant(
		name: String,
		configureAction: ResourceConfigurationVariant.() -> Unit = {}
	) {
		val resourceConfigurationVariant =
			objects.newInstance(
				ResourceConfigurationVariant::class.java,
				name,
				configurationAttributesConfigureAction,
			)

		configureAction(resourceConfigurationVariant)

		with(resourceConfigurationVariant) {

			val theConsumableConfigurationName =
				consumableConfigurationName.get()
			val theResourceDirectory = resourceDirectory.get()

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(theConsumableConfigurationName)
				.configure {
					// TODO: Remove debug logging.
					try {
						throw RuntimeException(
							"Configuration configureAction stacktrace"
						)
					} catch (e: RuntimeException) {
						logger.warn(
							"Configuring $theConsumableConfigurationName...", e
						)
					}

					attributes {
						applyVariantAttributes()
					}
				}

			// Any files in "src/main/<resourceConfigurationName>/" are
			// resources for this configuration. No build step is necessary, so
			// directly add the directory as a project artifact.
			if (theResourceDirectory.asFile.exists()) {
				project.artifacts.add(
					theConsumableConfigurationName,
					theResourceDirectory
				) {
					type = JVM_RESOURCES_DIRECTORY
				}
			}
		}

		resourceConfigurationVariants += resourceConfigurationVariant
	}

	/**
	 * Retrieves the resource configuration variant with the specified name.
	 *
	 * @param name the name of the resource configuration variant to retrieve.
	 * @return the corresponding resource configuration variant.
	 */
	operator fun get(name: String): ResourceConfigurationVariant =
		resourceConfigurationVariants.getByName(name)

	/**
	 * Checks whether the resource configuration variants collection is empty.
	 *
	 * @return true if there are no resource configuration variants, false
	 * otherwise.
	 */
	fun isEmpty(): Boolean =
		resourceConfigurationVariants.isEmpty()
}
