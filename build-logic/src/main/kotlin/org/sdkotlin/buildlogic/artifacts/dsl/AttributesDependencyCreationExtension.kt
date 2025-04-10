package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.slf4j.LoggerFactory

/**
 * A [DependencyHandler] extension that sets the attributes for variants of a
 * declared [Dependency].
 */
abstract class AttributesDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val providers: ProviderFactory,
	private val attributesConfigureAction: AttributeContainer.() -> Unit,
) : DependencyCreationExtension {

	private val logger = LoggerFactory.getLogger(this::class.java)

	override fun invoke(notation: Any): Provider<Dependency> =
		providers.provider {

			// TODO: Remove debug logging.
			logger.warn(
				"Creating $notation...",
				RuntimeException("Attributes dependency helper stacktrace")
			)

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
