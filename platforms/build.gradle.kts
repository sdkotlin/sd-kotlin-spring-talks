import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

tasks {
	// `checkConstraints` is read in the build that resolves, so it is declared
	// here as well as in the root build. The platforms' constraints are what
	// this build declares.
	named<DependencyUpdatesTask>("dependencyUpdates").configure {
		checkConstraints = true
		// kotlinx-datetime publishes `-0.6.x-compat` artifacts for migrating
		// from the 0.6 API. They sort after the plain release but are not updates.
		rejectVersionIf { candidate.version.endsWith("-0.6.x-compat") }
	}
}
