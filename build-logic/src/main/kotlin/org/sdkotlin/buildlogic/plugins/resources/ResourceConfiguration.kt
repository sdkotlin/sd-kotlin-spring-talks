package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.LIBRARY
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.named
import org.sdkotlin.buildlogic.attributes.MapBackedAttributeContainer
import javax.inject.Inject

/**
 * A project extension for configuring the [ResourceConfigurationsPlugin].
 *
 * @property name the resource configuration name. Used to derive the default
 * values for other properties.
 * @param objects the injected [ObjectFactory].
 * @param layout the injected [ProjectLayout].
 */
abstract class ResourceConfiguration @Inject constructor(
	val name: String,
	objects: ObjectFactory,
) {
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
	 * The name of the [DependencyHandler] extension added for declaring
	 * dependencies on a specific resource configuration.
	 *
	 * By default, the value is set to "<[name]>Resources".
	 */
	val dependencyHandlerExtensionName: Property<String> =
		objects.property(String::class.java)
			.convention("${name}Resources")

	@get:Nested
	val resourceAttributes: AttributeContainer =
		objects.newInstance(
			MapBackedAttributeContainer::class.java,
		).convention(
			mapOf(
				CATEGORY_ATTRIBUTE to objects.named<Category>(LIBRARY),
				BUNDLING_ATTRIBUTE to objects.named<Bundling>(EXTERNAL),
				LIBRARY_ELEMENTS_ATTRIBUTE to objects.named<LibraryElements>(
					libraryElementsAttributeValue.get()),
				USAGE_ATTRIBUTE to objects.named<Usage>(JAVA_RUNTIME),
			)
		)

	/**
	 * Configures the attributes for the resource configuration.
	 *
	 * If any attributes are configured, none of the default attributes will be
	 * applied, and must be redeclared as needed. This is to ensure precise,
	 * unambiguous definition of variants.
	 *
	 * @param action a configuration block that defines the attributes.
	 */
	fun attributes(action: AttributeContainer.() -> Unit) =
		resourceAttributes.action()

	@get:Nested
	val resourceConfigurationVariants: ResourceConfigurationVariants =
		objects.newInstance(
			ResourceConfigurationVariants::class.java,
			this,
		)

	/**
	 * Configures the variants for the resource configuration.
	 *
	 * If no variants are configured, defaults to a single variant with the
	 * attributes of this resource configuration.
	 *
	 * @param action a configuration block that creates the variants.
	 */
	fun variants(action: ResourceConfigurationVariants.() -> Unit) =
		resourceConfigurationVariants.action()
}
