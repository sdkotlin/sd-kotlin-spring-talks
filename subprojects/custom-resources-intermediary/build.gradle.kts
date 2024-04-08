plugins {
	id("org.sdkotlin.buildlogic.custom-resources-convention")
}

description =
	"An intermediary project to ensure custom resource are resolved transitively."

dependencies {
	// A project with a custom resources artifact.
	customScope(projects.subprojects.customResources)
}
