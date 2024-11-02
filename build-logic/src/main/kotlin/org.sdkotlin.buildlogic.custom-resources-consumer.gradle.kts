import org.sdkotlin.buildlogic.artifacts.dsl.DependencyCreationExtension
import org.sdkotlin.buildlogic.attributes.CustomResources.CUSTOM_RESOURCES
import org.sdkotlin.buildlogic.attributes.LibraryElementsAttributeDependencyCreationExtension

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
