package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.model.ObjectFactory
import org.sdkotlin.buildlogic.attributes.applyAttributes
import javax.inject.Inject

abstract class ResourceConfigurationVariants @Inject constructor(
	private val resourceConfiguration: ResourceConfiguration,
	private val project: Project,
	private val objects: ObjectFactory,
) {
	private val resourceConfigurationVariants:
		NamedDomainObjectSet<ResourceConfigurationVariant> =
		objects.namedDomainObjectSet(ResourceConfigurationVariant::class.java)

	fun variant(
		name: String,
		action: ResourceConfigurationVariant.() -> Unit = {}
	) {
		val resourceConfigurationVariant =
			objects.newInstance(
				ResourceConfigurationVariant::class.java,
				name,
			)

		resourceConfigurationVariant.action()

		with(resourceConfigurationVariant) {

			val theConsumableConfigurationName =
				consumableConfigurationName.get()
			val theResourceDirectory = resourceDirectory.get()

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(theConsumableConfigurationName)
				.configure {
					attributes {
						applyAttributes(resourceConfiguration.resourceAttributes)
						applyAttributes(variantAttributes)
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

	fun isEmpty(): Boolean =
		resourceConfigurationVariants.isEmpty()
}
