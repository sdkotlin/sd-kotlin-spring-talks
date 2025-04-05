package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Gradle plugin for registering named resource variants in support of
 * variant-aware sharing from producer projects to consumer projects.
 *
 * Named configurations can be created via the `resourceConfigurations` project
 * extension.
 *
 * ```kotlin
 * resourceConfigurations {
 *     create("special")
 * }
 * ```
 *
 * Multiple configurations can be created via additional `create("<name>")`
 * calls.
 *
 * Multiple variants of a configuration can be created:
 *
 * ```kotlin
 * resourceConfigurations {
 *     create("native") {
 *         variants {
 *             variant("linux") {
 *                 attributes {
 *                     [...]
 *                 }
 *             }
 *             variant("macos") {
 *                 attributes {
 *                     [...]
 *                 }
 *             }
 *             [...]
 *         }
 *     }
 * }
 * ```
 *
 * If no variants are declared, a default variant with the same name of the
 * configuration is added.
 *
 * Each variant by default adds a source directory of the same name under
 * 'src/main'. For a configuration named "special", it would be
 * 'src/main/special'. Any resources in this directory will be added as an
 * additional project output.
 *
 * Each configuration and variant can define its own attributes for that project
 * artifact, enabling variant-aware consumption by other projects. By default,
 * the following attributes are set:
 * ```
 * org.gradle.category            = library
 * org.gradle.dependency.bundling = external
 * org.gradle.libraryelements     = <name>-resources
 * org.gradle.usage               = java-runtime
 * ```
 *
 * For a resource configuration named "special", the
 * `org.gradle.libraryelements` attribute value would be set to
 * "special-resources".
 *
 * The default attributes can be overridden by the configuration:
 *
 * ```kotlin
 * resourceConfigurations {
 *     create("special") {
 *         attributes {
 *             attribute(CATEGORY_ATTRIBUTE, objects.named(LIBRARY))
 *             attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
 *             attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("something-else"))
 *             attribute(USAGE_ATTRIBUTE, objects.named(JAVA_RUNTIME))
 *         }
 *     }
 * }
 * ```
 *
 * If any attributes are configured, none of the default attributes will be
 * applied, and must be redeclared as needed. This is to ensure precise,
 * unambiguous definition of variants.
 *
 * Attributes declared for variants are appended to those used for the
 * configuration as a whole.
 *
 * A dependency handler extension is added to assist consuming projects in
 * declaring a dependency on the resource variant. For a resource configuration
 * named "special", the handler extension would be named
 * `specialResources(...)` A consuming project could use it to add the resources
 * to an existing configuration, e.g.
 * `runtimeOnly(specialResources(project("producer")))`. With this example, the
 * resources would be on the runtime classpath of the consumer project (and any
 * downstream consumers), and thereby available for tasks such as `test` and
 * `run`.
 *
 * Not that the dependency handler extension only adds the attributes for the
 * base configuration to the dependency declaration. Any additional attributes
 * for declared variants must be addressed by adding them to the respective
 * resolvable configurations, or via attribute compatibility and disambiguation
 * rules. This allows for artifact views to then be used to select only the
 * artifacts in the runtime classpath that are from a given variant, or to
 * select all the artifacts on the runtime classpath that are _not_ from the
 * variant. A use case for this may be for creating distributions or generating
 * installers where the resources are located outside a typical 'lib' folder
 * (e.g. in a 'conf/special' or 'bin/special' folder), yet are still placed on
 * the runtime classpath of the application for portable, classpath-relative
 * loading.
 */
class ResourceConfigurationsPlugin : Plugin<Project> {

	companion object {
		const val RESOURCE_CONFIGURATIONS_EXTENSION_NAME =
			"resourceConfigurations"
	}

	override fun apply(project: Project) {

		// Add a project extension for declaration and configuration of
		// resource configurations.
		val resourceConfigurationsExtension: ResourceConfigurations =
			project.objects.newInstance(ResourceConfigurations::class.java)

		project.extensions.add(
			RESOURCE_CONFIGURATIONS_EXTENSION_NAME,
			resourceConfigurationsExtension
		)
	}
}
