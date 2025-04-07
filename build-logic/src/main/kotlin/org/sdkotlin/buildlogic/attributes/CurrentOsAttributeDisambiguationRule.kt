package org.sdkotlin.buildlogic.attributes

import org.gradle.api.attributes.AttributeDisambiguationRule
import org.gradle.api.attributes.MultipleCandidatesDetails
import org.gradle.api.model.ObjectFactory
import org.gradle.nativeplatform.OperatingSystemFamily
import javax.inject.Inject

/**
 * A rule to disambiguate between multiple candidates of the
 * [OperatingSystemFamily] attribute in Gradle. It ensures that the attribute
 * value most closely matching the current operating system is chosen.
 *
 * The current operating system's attribute value must be set in the companion
 * object property [currentOsAttributeValue] during the project configuration
 * phase. This value is then used to determine the closest match among the
 * provided candidates during dependency resolution.
 *
 * @param currentOsAttributeValue the [OperatingSystemFamily] attribute value corresponding
 * to the current operating system.
 * @param objects the Gradle [ObjectFactory].
 */
open class CurrentOsAttributeDisambiguationRule @Inject constructor(
	private val currentOsAttribute: OperatingSystemFamily,
) : AttributeDisambiguationRule<OperatingSystemFamily> {

	override fun execute(
		details: MultipleCandidatesDetails<OperatingSystemFamily>
	) {
		if (details.candidateValues.contains(currentOsAttribute)) {
			details.closestMatch(currentOsAttribute)
		}
	}
}
