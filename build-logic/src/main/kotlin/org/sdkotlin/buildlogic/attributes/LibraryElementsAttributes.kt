package org.sdkotlin.buildlogic.attributes

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes

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
		libraryElementsAttributeValue: String
	) {
		attribute(USAGE_ATTRIBUTE, objects.named(JAVA_RUNTIME))
		attribute(CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
		attribute(LIBRARY_ELEMENTS_ATTRIBUTE,
			objects.named(libraryElementsAttributeValue))
		attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
	}
}

/**
 * A [DependencyHandler] extension that sets the standard attributes for
 * [LIBRARY_ELEMENTS_ATTRIBUTE]-based variants of a declared [Dependency].
 */
class LibraryElementsAttributeDependencyCreationExtension(
	private val dependencyHandler: DependencyHandler,
	private val objects: ObjectFactory,
	private val libraryElementsAttributeValue: String
) : DependencyCreationExtension {

	override fun invoke(notation: Any): Dependency {

		val dependency = dependencyHandler.create(notation)

		require(dependency is ProjectDependency) {
			"Dependency type ${dependency::class.qualifiedName} unknown!"
		}

		dependency.attributes {
			applyLibraryElementsAttributes(
				objects,
				libraryElementsAttributeValue
			)
		}

		return dependency
	}
}
