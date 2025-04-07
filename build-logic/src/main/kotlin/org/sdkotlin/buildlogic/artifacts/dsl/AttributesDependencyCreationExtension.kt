package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

/**
 * A [DependencyHandler] extension that sets the attributes for variants of a
 * declared [Dependency].
 */
class AttributesDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val providers: ProviderFactory,
	private val attributesConfigureAction: AttributeContainer.() -> Unit,
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Provider<Dependency> =
		providers.provider {

			println("Creating $notation...")

			val dependency = dependencyHandler.create(notation)

			require(dependency is ModuleDependency) {
				"Dependency type '${dependency::class.qualifiedName}' unknown!"
			}

			dependency.attributes {
				attributesConfigureAction()
			}

			dependency
		}
}
