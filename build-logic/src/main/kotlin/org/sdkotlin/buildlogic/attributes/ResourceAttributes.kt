package org.sdkotlin.buildlogic.attributes

import org.gradle.api.Named
import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.LibraryElements.RESOURCES
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.RESOURCE_ATTRIBUTE
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.applyResourceAttributes
import javax.inject.Inject

/**
 * An attribute type for resource variants.
 */
interface ResourceAttribute : Named {
	override fun getName(): String = "ResourceAttribute"
}

/**
 * A namespace for resource attribute constants and utilities.
 */
object ResourceAttributes {

	/**
	 * An attribute for resource dependency variants.
	 */
	val RESOURCE_ATTRIBUTE: Attribute<ResourceAttribute> =
		Attribute.of(
			ResourceAttributes::class.qualifiedName!!,
			ResourceAttribute::class.java
		)

	/**
	 * Helper function to set the standard attributes for resource dependency
	 * variants.
	 */
	fun AttributeContainer.applyResourceAttributes(
		objects: ObjectFactory,
		resourceAttributeValue: String
	) {
		attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
		attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(RESOURCES))
		attribute(RESOURCE_ATTRIBUTE, objects.named(resourceAttributeValue))
	}
}

/**
 * A [DependencyHandler] extension that sets the standard attributes for
 * resource variants to a declared [Dependency].
 */
class ResourceAttributeDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val objectFactory: ObjectFactory,
	private val resourceAttributeValue: String
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Dependency {

		val dependency = dependencyHandler.create(notation)

		require(dependency is ProjectDependency) {
			"Dependency type ${dependency::class.qualifiedName} unknown!"
		}

		dependency.attributes {
			applyResourceAttributes(
				objectFactory,
				resourceAttributeValue
			)
		}

		return dependency
	}
}

@CacheableRule
abstract class DefaultResourceAttributeRule : ComponentMetadataRule {

	@get:Inject
	abstract val objects: ObjectFactory

	override fun execute(context: ComponentMetadataContext) {
		context.details.allVariants {
			attributes {
				attributes.attribute(RESOURCE_ATTRIBUTE, objects.named("none"))
			}
		}
	}
}
