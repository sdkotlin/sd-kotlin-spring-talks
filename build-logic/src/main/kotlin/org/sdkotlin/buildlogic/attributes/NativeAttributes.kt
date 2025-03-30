package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE

/**
 * A namespace for native platform attribute utilities.
 */
object NativeAttributes {

	/**
	 * Helper function to set the standard attributes for
	 * [OperatingSystemFamily]-based variants.
	 */
	fun AttributeContainer.applyNativeAttributes(
		objects: ObjectFactory,
		operatingSystemAttributeValue: String,
	) {
		attribute(
			OPERATING_SYSTEM_ATTRIBUTE,
			objects.named(operatingSystemAttributeValue)
		)
	}
}
