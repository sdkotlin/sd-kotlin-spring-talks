plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	id("org.sdkotlin.buildlogic.custom-resources-consumer")
}

description =
	"An intermediary project to ensure custom resource are resolved transitively."

dependencies {
	// A project with a "custom" resources artifact.
	runtimeOnly(customResources(projects.subprojects.customResources))
}
