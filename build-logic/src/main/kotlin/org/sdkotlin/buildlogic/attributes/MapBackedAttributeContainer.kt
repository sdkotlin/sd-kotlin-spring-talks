package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import javax.inject.Inject

/**
 * An [AttributeContainer] backed by a [MapProperty].
 *
 * Supports setting a default for the contained attributes by passing a [Map] to
 * the [convention] function (similar to [Property]).
 *
 * @param objects the injected [ObjectFactory].
 */
open class MapBackedAttributeContainer @Inject constructor(
	objects: ObjectFactory,
) : AttributeContainer {

	private val resourceAttributes: MapProperty<Attribute<*>, Any> =
		objects.mapProperty(Attribute::class.java, Any::class.java)

	/**
	 * Sets default attributes for the container by applying the specified map
	 * of attributes.
	 *
	 * This function allows setting a convention for the attributes, which
	 * defines their default values. If a value for a specific attribute has
	 * already been set, the convention will not overwrite it.
	 *
	 * @param attributes a map of attributes and their default values to apply
	 * as the convention.
	 * @return the current instance of the attribute container, enabling method
	 * chaining.
	 */
	fun convention(attributes: Map<Attribute<*>, Any>): AttributeContainer {
		resourceAttributes.convention(attributes)
		return this
	}

	override fun keySet(): Set<Attribute<out Any>> =
		resourceAttributes.get().keys

	override fun <T : Any> attribute(
		key: Attribute<T>,
		value: T,
	): MapBackedAttributeContainer {
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

	override fun toString(): String =
		resourceAttributes.orNull?.toString() ?: "{}"
}
