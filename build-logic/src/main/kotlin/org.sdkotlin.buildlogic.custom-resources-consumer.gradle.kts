import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCE
import org.sdkotlin.buildlogic.attributes.DefaultResourceAttributeRule
import org.sdkotlin.buildlogic.attributes.ResourceAttributeDependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.RESOURCE_ATTRIBUTE

dependencies {

	attributesSchema {
		// Register a new attribute key for variant-aware consumption of
		// "custom" resources dependencies.
		attribute(RESOURCE_ATTRIBUTE)
	}

	components {
		// Add the resource attribute to all components with a default
		// value of "none".
		all<DefaultResourceAttributeRule>()
	}

	// Add a `DependencyHandler` extension for declaring dependencies on
	// artifacts with "custom" resources attributes.
	extensions.add(
		DependencyCreationExtension::class.java,
		"customResources",
		ResourceAttributeDependencyCreationExtension(
			dependencyHandler = this,
			objects = objects,
			resourceAttributeValue = CUSTOM_RESOURCE
		)
	)
}
