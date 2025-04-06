package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.sdkotlin.buildlogic.attributes.applyLibraryElementsAttributes
import javax.inject.Inject

/**
 * A project extension for configuring the [ResourceConfigurationsPlugin].
 *
 * @property name the resource configuration name. Used to derive the default
 * values for other properties.
 * @param objects the injected [ObjectFactory].
 */
abstract class ResourceConfiguration @Inject constructor(
	val name: String,
	private val objects: ObjectFactory,
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

	private var configurationAttributesAction: AttributeContainer.() -> Unit = {
		applyLibraryElementsAttributes(
			objects,
			libraryElementsAttributeValue.get(),
		)
	}

	/**
	 * Configures the attributes for the resource configuration.
	 *
	 * If any attributes are configured, none of the default attributes will be
	 * applied, and must be redeclared as needed. This is to ensure precise,
	 * unambiguous definition of variants.
	 *
	 * @param configureAction a configuration block that defines the attributes.
	 */
	fun attributes(configureAction: AttributeContainer.() -> Unit) {
		configurationAttributesAction = configureAction
	}

	/**
	 * Applies the base resource configuration attributes to an
	 * `AttributeContainer`.
	 *
	 * Users would typically apply the attributes for a given variant or the
	 * default variant instead with
	 * [ResourceConfigurationVariant.applyVariantAttributes].
	 */
	fun AttributeContainer.applyConfigurationAttributes() =
		configurationAttributesAction(this)

	val resourceConfigurationVariants: ResourceConfigurationVariants =
		objects.newInstance(
			ResourceConfigurationVariants::class.java,
			configurationAttributesAction,
		)

	/**
	 * Configures the variants for the resource configuration.
	 *
	 * If no variants are configured, defaults to a single variant with the
	 * attributes of this resource configuration.
	 *
	 * @param configureAction a configuration block that creates the variants.
	 */
	fun variants(configureAction: ResourceConfigurationVariants.() -> Unit) =
		configureAction(resourceConfigurationVariants)
}
