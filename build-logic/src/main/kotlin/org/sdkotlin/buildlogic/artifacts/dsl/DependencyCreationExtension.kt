package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * An invokable interface for [DependencyHandler] extensions that create
 * dependencies. Facilitates calling the extensions as unqualified helper
 * functions.
 */
fun interface DependencyCreationExtension {
	operator fun invoke(notation: Any): Dependency
}
