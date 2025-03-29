plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	id("org.sdkotlin.buildlogic.native-resources")
}

description =
	"An intermediary project to ensure native resources are resolved transitively."

dependencies {
	// A project with a native resources artifact.
	runtimeOnly(nativeResources(projects.subprojects.nativeResources))
}
