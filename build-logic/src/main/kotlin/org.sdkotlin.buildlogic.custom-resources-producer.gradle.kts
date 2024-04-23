import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCE
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.applyResourceAttributes

@Suppress("UnstableApiUsage")
configurations {

	// Create variant-aware consumable configuration for "custom" resources
	// artifacts.
	consumable("customElements") {
		attributes {
			applyResourceAttributes(objects, CUSTOM_RESOURCE)
		}
	}
}

artifacts {

	// Any files in "src/main/custom" are custom resources.
	val customResourceDirectory: Directory =
		project.layout.projectDirectory.dir("src/main/custom")

	//  No build step is necessary, so directly add the directory as an
	//  artifact to the variant-aware consumable configuration.
	add("customElements", customResourceDirectory)
}
