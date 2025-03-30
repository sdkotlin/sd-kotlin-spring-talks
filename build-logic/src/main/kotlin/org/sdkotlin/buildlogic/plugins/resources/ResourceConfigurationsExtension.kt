package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.attributes.AttributeContainer
import org.sdkotlin.buildlogic.artifacts.dsl.AttributesDependencyCreationExtension
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.applyAttributes
import javax.inject.Inject

abstract class ResourceConfigurationsExtension @Inject constructor(
	private val project: Project,
) {
	private val resourceConfigurations: NamedDomainObjectSet<ResourceConfiguration> =
		project.objects.namedDomainObjectSet(ResourceConfiguration::class.java)

	fun create(name: String, action: ResourceConfiguration.() -> Unit = {}) {

		val resourceConfiguration =
			project.objects.newInstance(ResourceConfiguration::class.java, name)

		resourceConfiguration.action()

		with(resourceConfiguration) {

			val resourceAttributesProvider =
				project.provider<AttributeContainer> { resourceAttributes }

			// Create a variant-aware consumable configuration for this resource
			// configuration's artifacts.
			@Suppress("UnstableApiUsage")
			project.configurations.consumable(consumableConfigurationName.get())
				.configure {
					attributes {

						// TODO: Remove debug logging.
						println(
							"Applying attributes for '$name' " +
								"to consumable configuration " +
								"'${consumableConfigurationName.get()}'..."
						)

						applyAttributes(resourceAttributesProvider)
					}
				}

			// TODO: Remove debug logging.
			println(
				"Checking if resource directory '${resourceDirectory.get()}' " +
					"exists for '$name'..."
			)

			// Any files in "src/main/<resourceConfigurationName>/" are
			// resources for this configuration. No build step is necessary, so
			// directly add the directory as a project artifact.
			if (resourceDirectory.get().asFile.exists()) {

				// TODO: Remove debug logging.
				println(
					"Adding resource directory '${resourceDirectory.get()}' " +
						"to project artifacts for '$name'..."
				)

				project.artifacts.add(
					consumableConfigurationName.get(),
					resourceDirectory
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
					resourceAttributesProvider,
				)
			)
		}

		resourceConfigurations += resourceConfiguration
	}

	operator fun get(name: String): ResourceConfiguration =
		resourceConfigurations.getByName(name)
}
