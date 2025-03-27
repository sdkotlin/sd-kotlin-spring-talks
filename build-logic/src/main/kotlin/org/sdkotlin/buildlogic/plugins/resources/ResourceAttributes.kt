package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class ResourceAttributes @Inject constructor(objects: ObjectFactory) :
	AttributeContainer {

	private val attributes: MapProperty<Attribute<*>, Any> =
		objects.mapProperty(Attribute::class.java, Any::class.java)
			.convention(emptyMap<Attribute<*>, Any>())

	override fun <T> attribute(
		key: Attribute<T>,
		value: T,
	): ResourceAttributes {
		attributes.put(key, value as Any)
		return this
	}

	override fun <T : Any?> attributeProvider(
		key: Attribute<T?>,
		provider: Provider<out T?>,
	): AttributeContainer {
		attributes.put(key, provider as Any)
		return this
	}

	override fun getAttributes(): AttributeContainer = this

	override fun isEmpty(): Boolean =
		attributes.get().isEmpty()

	override fun contains(key: Attribute<*>): Boolean =
		attributes.get().contains(key)

	override fun keySet(): Set<Attribute<*>?> =
		attributes.get().keys

	override fun <T : Any?> getAttribute(key: Attribute<T?>): T? =
		attributes.get()[key] as T?

	fun applyTo(target: AttributeContainer) {
		attributes.orNull?.forEach { (attr, value) ->
			target.attribute(attr as Attribute<Any>, value)
		}
	}
}
