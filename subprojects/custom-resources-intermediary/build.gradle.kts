import org.sdkotlin.buildlogic.attributes.ResourceAttributes.CUSTOM_RESOURCE
import org.sdkotlin.buildlogic.attributes.ResourceAttributes.applyResourceAttributes

plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	id("org.sdkotlin.buildlogic.custom-resources-convention")
}

description =
	"An intermediary project to ensure custom resource are resolved transitively."

dependencies {
	// A project with a custom resources artifact.
	runtimeOnly(projects.subprojects.customResources) {
		attributes {
			applyResourceAttributes(objects, CUSTOM_RESOURCE)
		}
	}
}
