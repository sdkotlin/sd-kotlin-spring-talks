package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.Action
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * Defines a configuration variant for resources within a project. Each variant
 * represents a specific set of attributes and a directory path where the
 * corresponding resource files are located. These variants allow for
 * variant-aware handling and consumption of resources by other projects.
 *
 * @property name The name of the resource configuration variant. Used to
 * determine default values for various properties, such as the resource
 * directory and consumable configuration name.
 * @constructor Creates an instance of the resource configuration variant with
 * the supplied name, project layout, and object factory.
 */
abstract class ResourceConfigurationVariant @Inject constructor(
	val name: String,
	layout: ProjectLayout,
	objects: ObjectFactory,
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
	 * Represents the attributes associated with a specific resource
	 * configuration variant.
	 */
	var variantAttributesAction: Action<in AttributeContainer> =
		Action {
			// Default to just the resource configuration attributes.
		}

	/**
	 * Configures the attributes for the resource configuration variant.
	 *
	 * If any attributes are configured, none of the default attributes will be
	 * applied, and must be redeclared as needed. This is to ensure precise,
	 * unambiguous definition of variants.
	 *
	 * @param configureAction a configuration block that defines the attributes.
	 */
	fun attributes(configureAction: Action<in AttributeContainer>) {
		variantAttributesAction = configureAction
	}
}
