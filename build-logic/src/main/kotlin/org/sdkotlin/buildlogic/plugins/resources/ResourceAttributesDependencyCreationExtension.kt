package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension

/**
 * A [DependencyHandler] extension that sets the attributes for variants of a
 * declared [Dependency].
 */
class ResourceAttributesDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val resourceAttributes: ResourceAttributes,
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Dependency {

		val dependency = dependencyHandler.create(notation)

		require(dependency is ProjectDependency) {
			"Dependency type ${dependency::class.qualifiedName} unknown!"
		}

		resourceAttributes.applyTo(dependency.attributes)

		return dependency
	}
}
