import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCE
import org.sdkotlin.buildlogic.attributes.ResourceAttributeDependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.RESOURCE_ATTRIBUTE

dependencies {

	// Register a new attribute key for variant-aware consumption
	// of "custom" resources dependencies.
	attributesSchema {
		attribute(RESOURCE_ATTRIBUTE)
	}

	// Add a `DependencyHandler` extension for declaring dependencies on
	// artifacts with "custom" resources attributes.
	extensions.add(
		DependencyCreationExtension::class.java,
		"customResources",
		ResourceAttributeDependencyCreationExtension(
			dependencyHandler = this,
			objectFactory = objects,
			resourceAttributeValue = CUSTOM_RESOURCE
		)
	)
}
