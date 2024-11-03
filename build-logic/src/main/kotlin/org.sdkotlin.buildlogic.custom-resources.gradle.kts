import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributeDependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributes.applyLibraryElementsAttributes

@Suppress("UnstableApiUsage")
configurations {

	// Create a variant-aware consumable configuration for "custom resources"
	// artifacts.
	consumable("customElements") {
		attributes {
			applyLibraryElementsAttributes(objects, CUSTOM_RESOURCES)
		}
	}
}

dependencies {

	// Add a `DependencyHandler` extension for declaring a dependency on
	// the "custom resources" artifact variant.
	extensions.add(
		DependencyCreationExtension::class.java,
		"customResources",
		LibraryElementsAttributeDependencyCreationExtension(
			dependencyHandler = this,
			objects = objects,
			libraryElementsAttributeValue = CUSTOM_RESOURCES
		)
	)
}

artifacts {

	// Any files in "src/main/custom" are custom resources.
	val customResourceDirectory: Directory =
		project.layout.projectDirectory.dir("src/main/custom")

	// No build step is necessary, so directly add the directory as an
	// artifact to the variant-aware consumable configuration (if it exists).
	if (customResourceDirectory.asFile.exists()) {
		add("customElements", customResourceDirectory) {
			type = JVM_RESOURCES_DIRECTORY
		}
	}
}

tasks {

	register<PrintClasspath>("printRuntimeClasspath") {

		group = "custom-resources"
		description = "Prints the runtime classpath"

		classpathName = "runtimeClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get()
		}
	}

	register<PrintClasspath>("printCustomResourcesClasspath") {

		group = "custom-resources"
		description =
			"Prints the custom resources subset of the runtime classpath"

		classpathName = "customResourcesClasspath"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue = CUSTOM_RESOURCES
						)
					}
				}.files
		}
	}

	register<PrintClasspath>("printRuntimeClasspathWithoutCustomResources") {

		group = "custom-resources"
		description =
			"Prints the runtime classpath without the custom resources"

		classpathName = "runtimeClasspathWithoutCustomResources"

		classpath = provider {
			configurations.named("runtimeClasspath").get().incoming
				.artifactView {

					@Suppress("UnstableApiUsage")
					withVariantReselection()

					attributes {
						applyLibraryElementsAttributes(
							objects,
							libraryElementsAttributeValue = CLASSES_AND_RESOURCES
						)
					}
				}.files
		}
	}
}

@CacheableTask
abstract class PrintClasspath : DefaultTask() {

	@get:Input
	abstract val classpathName: Property<String>

	@get:Classpath
	abstract val classpath: Property<FileCollection>

	@TaskAction
	fun printClasspath() {

		val classpathAsPath = classpath.get().asPath

		val wrappedClasspath = classpathAsPath.replace(":", "\n")

		logger.lifecycle("${classpathName.get()}: \n$wrappedClasspath")
	}
}
