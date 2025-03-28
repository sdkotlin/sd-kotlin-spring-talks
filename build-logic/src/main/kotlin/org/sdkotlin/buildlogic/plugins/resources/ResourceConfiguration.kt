package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import javax.inject.Inject

/**
 * A project extension for use in configuring the
 * [ResourceConfigurationsPlugin].
 */
abstract class ResourceConfiguration @Inject constructor(
	val name: String,
	project: Project,
) {
	val resourceDirectory: DirectoryProperty =
		project.objects.directoryProperty()
			.convention(project.layout.projectDirectory.dir("src/main/$name"))

	internal val resourceAttributes: ResourceAttributes =
		project.objects.newInstance(ResourceAttributes::class.java, name)

	fun attributes(action: ResourceAttributes.() -> Unit) {
		resourceAttributes.action()
	}
}
