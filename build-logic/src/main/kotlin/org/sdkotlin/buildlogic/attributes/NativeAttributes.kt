package org.sdkotlin.buildlogic.attributes

import org.gradle.api.GradleException
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.named
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.MACOS
import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS

/**
 * A namespace for [OPERATING_SYSTEM_ATTRIBUTE] utilities.
 */
object NativeAttributes {

	/**
	 * Helper function to set the attributes for
	 * [OPERATING_SYSTEM_ATTRIBUTE]-based variants.
	 */
	fun AttributeContainer.applyNativeAttributes(
		objects: ObjectFactory,
		currentOs: Provider<String>
	) {
		val operatingSystemAttributeValue =
			when (currentOs.get()) {
				"linux" -> LINUX
				"osx" -> MACOS
				"windows" -> WINDOWS
				else ->
					throw GradleException(
						"Unknown operating system: '$currentOs'!"
					)
			}

		attribute(
			OPERATING_SYSTEM_ATTRIBUTE,
			objects.named(operatingSystemAttributeValue)
		)
	}
}
