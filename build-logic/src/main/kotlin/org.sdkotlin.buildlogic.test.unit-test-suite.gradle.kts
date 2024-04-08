plugins {
	kotlin("jvm")
	id("jvm-test-suite")
}

// Version catalog type-safe accessors not available in precompiled script
// plugins: https://github.com/gradle/gradle/issues/15383. Use the version
// catalog extension instead.
val versionCatalog = versionCatalogs.named("libs")

@Suppress("UnstableApiUsage")
testing {
	suites {
		configureEach {
			if (this is JvmTestSuite) {

				val junitVersion = versionCatalog.findVersion("junit")
					.get().preferredVersion

				useJUnitJupiter(junitVersion)
			}
		}
	}
}

dependencies {

	testImplementation(platform("org.sdkotlin.platforms:test-platform"))

	testImplementation(versionCatalog.findLibrary("assertj").get())
	testImplementation(versionCatalog.findLibrary("junit-api").get())
	testImplementation(versionCatalog.findLibrary("junit-params").get())

	testImplementation(versionCatalog.findLibrary("mockk").get())
	testImplementation(versionCatalog.findLibrary("mockk-dsl-jvm").get())

	// For MockK logging (https://github.com/mockk/mockk/issues/37).
	testRuntimeOnly(versionCatalog.findLibrary("log4j-core").get())
	testRuntimeOnly(versionCatalog.findLibrary("log4j-slf4j2-impl").get())
}
