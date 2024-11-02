import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes

plugins {
	kotlin("jvm")
	// Version catalog type-safe accessors and extension not yet available in
	// plugins block of precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383
	// alias(libs.plugins.dependency.analysis.gradle.plugin)
	id("com.autonomousapps.dependency-analysis")
	// alias(libs.plugins.jvm.dependency.conflict.detection.gradle.plugin)
	id("org.gradlex.jvm-dependency-conflict-detection")
	// alias(libs.plugins.jvm.dependency.conflict.resolution.gradle.plugin)
	id("org.gradlex.jvm-dependency-conflict-resolution")
}

val javaTargetVersion: String = JavaVersion.VERSION_21.toString()

jvmDependencyConflicts {
	logging {
		enforceLog4J2()
	}
}

tasks {
	withType<JavaCompile>().configureEach {

		with(options) {
			release = javaTargetVersion.toInt()
			isFork = true
		}
	}

	withType<KotlinCompile>().configureEach {
		compilerOptions {
			optIn = listOf(
				"kotlin.ExperimentalStdlibApi",
				"kotlin.contracts.ExperimentalContracts",
				"kotlinx.coroutines.ExperimentalCoroutinesApi",
			)
			// Planned for deprecation:
			// https://youtrack.jetbrains.com/issue/KT-61035/
			freeCompilerArgs = listOf(
				// https://youtrack.jetbrains.com/issue/KT-61410/
				"-Xjsr305=strict",
				// https://youtrack.jetbrains.com/issue/KT-49746/
				"-Xjdk-release=$javaTargetVersion"
			)
		}
	}

	register("printCustomResourcesClasspath") {

		group = "custom-resources"
		description =
			"Prints the custom resources subset of the runtime classpath"

		val fileCollection: Provider<FileCollection> = provider {
			configurations.runtimeClasspath.get().incoming.artifactView {

				@Suppress("UnstableApiUsage")
				withVariantReselection()

				attributes {
					applyLibraryElementsAttributes(objects, CUSTOM_RESOURCES)
				}
			}.files
		}

		doLast {

			val classpathAsPath = fileCollection.get().asPath

			val wrappedClasspath = classpathAsPath.replace(":", "\n")

			println("wrappedClasspath: \n$wrappedClasspath")
		}
	}

	register("printRuntimeClasspathWithoutCustomResources") {

		group = "custom-resources"
		description =
			"Prints the runtime classpath without the custom resources"

		val fileCollection: Provider<FileCollection> = provider {
			configurations.runtimeClasspath.get().incoming.artifactView {

				@Suppress("UnstableApiUsage")
				withVariantReselection()

				attributes {
					applyLibraryElementsAttributes(objects,
						CLASSES_AND_RESOURCES)
				}
			}.files
		}

		doLast {

			val classpathAsPath = fileCollection.get().asPath

			val wrappedClasspath = classpathAsPath.replace(":", "\n")

			println("wrappedClasspath: \n$wrappedClasspath")
		}
	}

	register("printRuntimeClasspath") {

		group = "custom-resources"
		description = "Prints the runtime classpath"

		val fileCollection: Provider<FileCollection> = provider {
			configurations.runtimeClasspath.get()
		}

		doLast {

			val classpathAsPath = fileCollection.get().asPath

			val wrappedClasspath = classpathAsPath.replace(":", "\n")

			println("wrappedClasspath: \n$wrappedClasspath")
		}
	}
}
