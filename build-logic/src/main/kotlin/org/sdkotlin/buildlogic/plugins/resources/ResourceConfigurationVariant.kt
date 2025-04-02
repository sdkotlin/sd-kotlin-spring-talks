package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.sdkotlin.buildlogic.attributes.MapBackedAttributeContainer
import javax.inject.Inject

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

	@get:Nested
	val variantAttributes: AttributeContainer =
		objects.newInstance(
			MapBackedAttributeContainer::class.java,
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
	fun attributes(action: AttributeContainer.() -> Unit) =
		variantAttributes.action()
}
