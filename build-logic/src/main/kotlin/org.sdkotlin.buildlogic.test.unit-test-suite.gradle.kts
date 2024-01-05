import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
	id("java")
	id("jvm-test-suite")
}

val versionCatalog = versionCatalogs.named("libs")

@Suppress("UnstableApiUsage")
testing {
	suites {
		configureEach {
			if (this is JvmTestSuite) {

				// Version catalog type-safe accessors not available in
				// precompiled script plugins:
				// https://github.com/gradle/gradle/issues/15383
				val junitVersion = versionCatalog.findVersion("junit")
					.get().preferredVersion

				useJUnitJupiter(junitVersion)

				targets {
					all {
						testTask.configure {
							testLogging {
								showStandardStreams = true
								events(
									PASSED,
									SKIPPED,
									FAILED,
									STANDARD_OUT,
									STANDARD_ERROR
								)
							}
						}
					}
				}
			}
		}
	}
}

dependencies {

	// Version catalog not available in precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383

	testImplementation(platform("org.sdkotlin.platforms:test-platform"))

	//testImplementation(libs.bundles.junit)
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testImplementation("org.junit.jupiter:junit-jupiter-api")

	//testImplementation(libs.assertj.core)
	testImplementation("org.assertj:assertj-core")

	//testImplementation(libs.bundles.mockk.jvm)
	testImplementation("io.mockk:mockk")
	testImplementation("io.mockk:mockk-dsl-jvm")

	// For MockK logging (https://github.com/mockk/mockk/issues/37).
	//testRuntimeOnly(libs.log4j.core)
	testRuntimeOnly("org.apache.logging.log4j:log4j-core")
	//testRuntimeOnly(libs.log4j.slf4j2.impl)
	testRuntimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
}
