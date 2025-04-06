package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.kotlin.dsl.invoke

/**
 * A [DependencyHandler] extension that sets the attributes for variants of a
 * declared [Dependency].
 */
class AttributesDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val attributesAction: Action<AttributeContainer>,
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Dependency {

		val dependency = dependencyHandler.create(notation)

		require(dependency is ModuleDependency) {
			"Dependency type '${dependency::class.qualifiedName}' unknown!"
		}

		attributesAction(dependency.attributes)

		return dependency
	}
}
