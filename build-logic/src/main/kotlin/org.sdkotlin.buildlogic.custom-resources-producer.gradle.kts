import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCES
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

artifacts {

	// Any files in "src/main/custom" are custom resources.
	val customResourceDirectory: Directory =
		project.layout.projectDirectory.dir("src/main/custom")

	// No build step is necessary, so directly add the directory as an
	// artifact to the variant-aware consumable configuration.
	add("customElements", customResourceDirectory) {
		type = JVM_RESOURCES_DIRECTORY
	}
}
