package org.sdkotlin.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * A custom Gradle task that prints the contents of a classpath in a
 * human-readable format.
 *
 * The task takes a classpath name and a classpath definition as inputs.
 * When executed, it prints the name of the classpath followed by the
 * individual entries of the classpath on new lines. This is primarily
 * used for debugging or inspecting the contents of a classpath in a
 * build script.
 *
 * This task is cacheable. Its output is only computed if its inputs have
 * changed.
 *
 * Properties:
 * - `classpathName`: The name of the classpath to display.
 * - `classpath`: The classpath represented as a [FileCollection].
 *
 * Task action:
 * - The [printClasspath] method formats the classpath entries into a more
 *   readable form by replacing the file path separator with new lines,
 *   then logs the formatted output at the `lifecycle` logging level.
 */
@CacheableTask
abstract class PrintClasspath : DefaultTask() {

	@get:Input
	abstract val classpathName: Property<String>

	@get:Classpath
	abstract val classpath: Property<FileCollection>

	@TaskAction
	fun printClasspath() {

		val classpathAsPath = classpath.get().asPath

		val wrappedClasspath =
			classpathAsPath.replace(File.pathSeparator, "\n")

		logger.lifecycle("${classpathName.get()}: \n$wrappedClasspath")
	}
}
