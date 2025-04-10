package org.sdkotlin.buildlogic.artifacts.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider

/**
 * An invokable interface for [DependencyHandler] extensions that create
 * dependencies. Facilitates calling the extensions as unqualified helper
 * functions.
 */
interface DependencyCreationExtension : ExtensionAware {

	/**
	 * Invokable from the Kotlin DSL.
	 */
	operator fun invoke(notation: Any): Provider<Dependency>

	/**
	 * Invokable from the Groovy DSL.
	 */
	@Suppress("unused")
	fun call(notation: Any): Provider<Dependency> = invoke(notation)
}
