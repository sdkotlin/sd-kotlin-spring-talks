package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.LIBRARY
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.named

/**
 * A namespace for custom [LIBRARY_ELEMENTS_ATTRIBUTE] utilities.
 */
object LibraryElementsAttributes {

	/**
	 * Helper function to set the ecosystem-independent standard attributes for
	 * [LIBRARY_ELEMENTS_ATTRIBUTE]-based variants.
	 */
	fun AttributeContainer.applyLibraryElementsAttributes(
		objects: ObjectFactory,
		libraryElementsAttributeValue: Provider<String>
	) {
		attribute(CATEGORY_ATTRIBUTE, objects.named(LIBRARY))
		attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
		attribute(LIBRARY_ELEMENTS_ATTRIBUTE,
			objects.named(libraryElementsAttributeValue.get()))
		attribute(USAGE_ATTRIBUTE, objects.named(JAVA_RUNTIME))
	}
}
