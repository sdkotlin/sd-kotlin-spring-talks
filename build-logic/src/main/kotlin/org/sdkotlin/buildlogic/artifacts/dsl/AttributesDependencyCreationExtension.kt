package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.sdkotlin.buildlogic.attributes.applyAttributes

/**
 * A [DependencyHandler] extension that sets the attributes for variants of a
 * declared [Dependency].
 */
class AttributesDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val dependencyAttributes: AttributeContainer,
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Dependency {

		val dependency = dependencyHandler.create(notation)

		require(dependency is ModuleDependency) {
			"Dependency type ${dependency::class.qualifiedName} unknown!"
		}

		dependency.attributes {
			// TODO: Remove debug logging.
			println("Applying attributes for dependency '$dependency'...")

			applyAttributes(dependencyAttributes)
		}

		return dependency
	}
}
