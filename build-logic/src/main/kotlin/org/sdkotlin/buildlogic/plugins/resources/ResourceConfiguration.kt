package org.sdkotlin.buildlogic.plugins.resources

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class ResourceConfiguration @Inject constructor(
	val name: String,
	project: Project,
) {
	val resourceDirectory: DirectoryProperty =
		project.objects.directoryProperty()
			.convention(project.layout.projectDirectory.dir("src/main/$name"))

	val libraryElementsAttributeValue: Property<String> =
		project.objects.property<String>()
			.convention("$name-resources")
}
