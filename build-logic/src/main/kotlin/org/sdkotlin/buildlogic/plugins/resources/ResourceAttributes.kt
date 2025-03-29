package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.LIBRARY
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.named
import javax.inject.Inject

/**
 * An [AttributeContainer] for configuring the attributes of resource
 * configuration variants.
 *
 * If not otherwise configured, the following attributes are set:
 * ```
 * org.gradle.category            = library
 * org.gradle.dependency.bundling = external
 * org.gradle.libraryelements     = <libraryElementsAttributeValue>
 * org.gradle.usage               = java-runtime
 * ```
 *
 * @param libraryElementsAttributeValue the value used for the
 * `org.gradle.libraryelements` attribute, if the default attributes are not
 * overridden.
 * @param objects the injected [ObjectFactory].
 */
abstract class ResourceAttributes @Inject constructor(
	libraryElementsAttributeValue: Provider<String>,
	objects: ObjectFactory,
) : AttributeContainer {

	private val resourceAttributes: MapProperty<Attribute<*>, Any> =
		objects.mapProperty(Attribute::class.java, Any::class.java)
			.convention(
				mapOf<Attribute<*>, Any>(
					CATEGORY_ATTRIBUTE to objects.named<Category>(LIBRARY),
					BUNDLING_ATTRIBUTE to objects.named<Bundling>(EXTERNAL),
					LIBRARY_ELEMENTS_ATTRIBUTE to
						objects.named<LibraryElements>(
							libraryElementsAttributeValue.get()
						),
					USAGE_ATTRIBUTE to objects.named<Usage>(JAVA_RUNTIME),
				)
			)

	override fun keySet(): Set<Attribute<out Any>> =
		resourceAttributes.get().keys

	override fun <T : Any> attribute(
		key: Attribute<T>,
		value: T,
	): ResourceAttributes {
		resourceAttributes.put(key, value as Any)
		return this
	}

	override fun <T : Any> attributeProvider(
		key: Attribute<T>,
		provider: Provider<out T>,
	): AttributeContainer {
		resourceAttributes.put(key, provider as Any)
		return this
	}

	override fun <T : Any> getAttribute(key: Attribute<T>): T {
		@Suppress("UNCHECKED_CAST")
		return resourceAttributes.get()[key] as T
	}

	override fun isEmpty(): Boolean =
		resourceAttributes.get().isEmpty()

	override fun contains(key: Attribute<*>): Boolean =
		resourceAttributes.get().contains(key)

	override fun getAttributes(): AttributeContainer = this
}
