package org.sdkotlin.buildlogic.osdetector

import com.google.gradle.osdetector.OsDetector
import org.gradle.api.GradleException
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.MACOS
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS

/**
 * Converts the [OsDetector] detected operating system into a standard
 * [OperatingSystemFamily] attribute value string.
 *
 * @return A string representing the operating system attribute value.
 * @throws GradleException if the detected operating system is unrecognized.
 */
fun OsDetector.osAsOperatingSystemFamilyAttributeValue(): String =
	when (os) {
		"linux" -> LINUX
		"osx" -> MACOS
		"windows" -> WINDOWS
		else ->
			throw GradleException(
				"Unknown operating system: '$os'!"
			)
	}
