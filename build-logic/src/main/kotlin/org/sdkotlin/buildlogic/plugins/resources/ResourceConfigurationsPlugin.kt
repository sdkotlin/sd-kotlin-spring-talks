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
 * Multiple configurations can be created via additional `create("<name>")` calls.
 *
 * Each named configuration adds a source directory of the same name under
 * 'src/main'. For a configuration named "special", it would be
 * 'src/main/special'. Any resources in this directory will be added as an
 * additional project output.
 *
 * Each configuration can define its own attributes for that project output,
 * enabling variant-aware consumption by other projects. By default, the
 * following attributes are set:
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
 *         attribute(CATEGORY_ATTRIBUTE, objects.named(LIBRARY))
 *         attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
 *         attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("something-else"))
 *         attribute(USAGE_ATTRIBUTE, objects.named(JAVA_RUNTIME))
 *     }
 * }
 * ```
 *
 * If any attributes are configured, none of the default attributes will be
 * applied, and must be redeclared as needed. This is to ensure precise,
 * unambiguous definition of variants.
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
 * Artifact views could then be used to select only the artifacts in
 * the runtime classpath that are from the named resource configuration, or to
 * select all the artifacts on the runtime classpath that are _not_ from the
 * named resource configuration. A use case for this may be for creating
 * distributions or generating installers where the resources are located
 * outside a typical 'lib' folder (e.g. in a 'conf/special' or 'bin/special'
 * folder), yet are still placed on the runtime classpath of the
 * application for portable, classpath-relative loading.
 */
class ResourceConfigurationsPlugin : Plugin<Project> {

	companion object {
		const val RESOURCE_CONFIGURATION_EXTENSION_NAME =
			"resourceConfiguration"
	}

	override fun apply(project: Project) {

		// Add a project extension for declaration and configuration of
		// resource configurations.
		val resourceConfigurationsExtension: ResourceConfigurationsExtension =
			project.objects
				.newInstance(ResourceConfigurationsExtension::class.java)

		project.extensions.add(
			RESOURCE_CONFIGURATION_EXTENSION_NAME,
			resourceConfigurationsExtension
		)
	}
}
