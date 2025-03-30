package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.provider.Provider

/**
 * An extension function to assist with applying the attributes from one
 * [AttributeContainer] to another.
 *
 * @param otherAttributeContainer the `AttributeContainer` to apply the
 * attributes to.
 */
fun AttributeContainer.applyAttributes(
	otherAttributeContainer: Provider<AttributeContainer>,
) {
	val theOtherAttributeContainer = otherAttributeContainer.get()

	// TODO: Removed debug logging.
	println("Applying attributes $theOtherAttributeContainer")

	theOtherAttributeContainer.keySet().forEach { key ->
		@Suppress("UNCHECKED_CAST")
		val value =
			theOtherAttributeContainer.getAttribute<Any>(key as Attribute<Any>)
		attribute(key, value as Any)
	}
}
