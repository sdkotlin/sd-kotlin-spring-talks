package org.sdkotlin.buildlogic.attributes

import org.gradle.api.Named
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.LibraryElements.RESOURCES
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named

/**
 * An attribute type for resource variants.
 */
interface ResourceAttributeType : Named {
	override fun getName(): String = "ResourceAttribute"
}

object ResourceAttributes {

	/**
	 * The [RESOURCE_ATTRIBUTE] value for the "custom" resource dependency
	 * variant.
	 */
	const val CUSTOM_RESOURCE = "Custom"

	/**
	 * An attribute for resource dependency variants.
	 */
	val RESOURCE_ATTRIBUTE: Attribute<ResourceAttributeType> =
		Attribute.of(
			ResourceAttributes::class.qualifiedName!!,
			ResourceAttributeType::class.java
		)

	/**
	 * Helper function to apply the standard attributes for resource dependency
	 * variants.
	 */
	fun AttributeContainer.applyResourceAttributes(
		objectFactory: ObjectFactory,
		resourceAttributeValue: String
	) {
		attribute(BUNDLING_ATTRIBUTE, objectFactory.named(EXTERNAL))
		attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objectFactory.named(RESOURCES))
		attribute(RESOURCE_ATTRIBUTE,
			objectFactory.named(resourceAttributeValue))
	}
}
