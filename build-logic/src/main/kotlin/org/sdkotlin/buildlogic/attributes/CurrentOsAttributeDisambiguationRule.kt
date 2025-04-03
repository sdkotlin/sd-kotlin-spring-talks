package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.AttributeDisambiguationRule
import org.gradle.api.attributes.MultipleCandidatesDetails
import org.gradle.nativeplatform.OperatingSystemFamily
import org.sdkotlin.buildlogic.attributes.CurrentOsAttributeDisambiguationRule.Companion.currentOsAttributeValue

/**
 * A rule to disambiguate between multiple candidates of the
 * [OperatingSystemFamily] attribute in Gradle. It ensures that the attribute
 * value most closely matching the current operating system is chosen.
 *
 * The current operating system's attribute value must be set in the companion
 * object property [currentOsAttributeValue] during the project configuration
 * phase. This value is then used to determine the closest match among the
 * provided candidates during dependency resolution.
 */
abstract class CurrentOsAttributeDisambiguationRule :
	AttributeDisambiguationRule<OperatingSystemFamily> {

	companion object {

		/**
		 * Represents the [OperatingSystemFamily] attribute value corresponding
		 * to the current operating system. This variable must be initialized
		 * during the Gradle project configuration phase to ensure that
		 * dependency resolution prioritizes candidates that match the current
		 * operating system's attribute.
		 */
		lateinit var currentOsAttributeValue: OperatingSystemFamily
	}

	override fun execute(
		details: MultipleCandidatesDetails<OperatingSystemFamily>
	) {
		details.closestMatch(currentOsAttributeValue)
	}
}
