package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * A project extension for configuring the [ResourceConfigurationsPlugin].
 *
 * @property name the resource configuration name. Used to derive the default
 * values for other properties.
 * @param objects the injected [ObjectFactory].
 * @param layout the injected [ProjectLayout]
 */
abstract class ResourceConfiguration @Inject constructor(
	val name: String,
	objects: ObjectFactory,
	layout: ProjectLayout,
) {

	/**
	 * Represents the directory where resource files for a specific resource
	 * configuration are located. This directory is used to add resource files
	 * as project artifacts, enabling variant-aware handling and consumption of
	 * these resources by other projects.
	 *
	 * By default, this directory is set to "src/main/<[name]>".
	 */
	val resourceDirectory: DirectoryProperty =
		objects.directoryProperty()
			.convention(layout.projectDirectory.dir("src/main/$name"))

	/**
	 * Specifies the value for the `org.gradle.libraryelements` attribute of the
	 * resource configuration. This attribute is utilized to describe the type
	 * of library elements that are produced or consumed by the resource
	 * configuration.
	 *
	 * By default, the value is set to "<[name]>-resources".
	 */
	val libraryElementsAttributeValue: Property<String> =
		objects.property(String::class.java)
			.convention("$name-resources")

	/**
	 * The name of the consumable configuration created for a resource
	 * configuration.
	 *
	 * The configuration is used to make the artifacts of the resource
	 * configuration available for consumption by other projects.
	 *
	 * By default, the value is set to "<[name]>Elements".
	 */
	val consumableConfigurationName: Property<String> =
		objects.property(String::class.java)
			.convention("${name}Elements")

	/**
	 * The name of the [DependencyHandler] extension added for declaring
	 * dependencies on a specific resource configuration.
	 *
	 * By default, the value is set to "<[name]>Resources".
	 */
	val dependencyHandlerExtensionName: Property<String> =
		objects.property(String::class.java)
			.convention("${name}Resources")

	internal val resourceAttributes: ResourceAttributes =
		objects.newInstance(
			ResourceAttributes::class.java,
			libraryElementsAttributeValue,
		)

	/**
	 * Configures the attributes for the resource configuration variant.
	 *
	 * If any attributes are configured, none of the default attributes will be
	 * applied, and must be redeclared as needed. This is to ensure precise,
	 * unambiguous definition of variants.
	 *
	 * @param action a configuration block that defines the attributes.
	 */
	fun attributes(action: ResourceAttributes.() -> Unit) {
		resourceAttributes.action()
	}
}
