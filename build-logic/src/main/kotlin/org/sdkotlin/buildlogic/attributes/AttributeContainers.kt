package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer

/**
 * An extension function to assist with applying the attributes from one
 * [AttributeContainer] to another.
 *
 * @param otherAttributeContainer the `AttributeContainer` to apply the
 * attributes to.
 */
fun AttributeContainer.applyAttributes(
	otherAttributeContainer: AttributeContainer,
) {
	otherAttributeContainer.keySet().forEach { key ->
		@Suppress("UNCHECKED_CAST")
		val value =
			otherAttributeContainer.getAttribute<Any>(key as Attribute<Any>)

		// TODO: Remove debug logging.
		println("Applying attribute '$key' with value '$value'...")

		attribute(key, value as Any)
	}
}
