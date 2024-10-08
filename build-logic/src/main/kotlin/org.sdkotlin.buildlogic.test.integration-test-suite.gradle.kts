import com.autonomousapps.DependencyAnalysisSubExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME
import org.sdkotlin.buildlogic.test.Compilations.TEST_FIXTURES_COMPILATION_NAME

plugins {
	kotlin("jvm")
	id("jvm-test-suite")
	id("org.sdkotlin.buildlogic.test.unit-test-suite")
}

val testSuiteName = "integrationTest"
val testSuiteDirName = "it"
val testSuiteTestSuffix = "IT"

// Version catalog type-safe accessors not available in precompiled script
// plugins: https://github.com/gradle/gradle/issues/15383. Use the version
// catalog extension instead.
val versionCatalog = versionCatalogs.named("libs")

@Suppress("UnstableApiUsage")
testing {
	suites {

		val test by getting(JvmTestSuite::class)

		register<JvmTestSuite>(testSuiteName) {

			sources {
				val sourcesRootDir = "src/$testSuiteDirName"
				java {
					setSrcDirs(listOf("$sourcesRootDir/java"))
				}
				kotlin {
					setSrcDirs(
						listOf(
							"$sourcesRootDir/kotlin",
							"$sourcesRootDir/java",
						)
					)
				}
				resources {
					setSrcDirs(listOf("$sourcesRootDir/resources"))
				}
			}

			targets {
				all {
					testTask.configure {
						filter {
							includeTestsMatching("*$testSuiteTestSuffix")
							// Support JUnit @Nested tests
							includeTestsMatching("*$testSuiteTestSuffix$*")
						}
						shouldRunAfter(test)
					}
				}
			}
		}
	}
}

dependencies {

	"integrationTestImplementation"(
		platform("org.sdkotlin.platforms:test-platform")
	)

	"integrationTestImplementation"(
		versionCatalog.findLibrary("assertj").get()
	)
	"integrationTestImplementation"(
		versionCatalog.findLibrary("junit-api").get()
	)
}

configure<DependencyAnalysisSubExtension> {
	abi {
		exclusions {
			excludeSourceSets(testSuiteName)
		}
	}
	issues {
		// Ignore test source set to work around
		// https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/1239.
		ignoreSourceSet(testSuiteName)
	}
}

kotlin {
	target {
		// Workaround for https://youtrack.jetbrains.com/issue/KTIJ-23114.
		compilations.getByName(testSuiteName)
			.associateWith(compilations.getByName(MAIN_COMPILATION_NAME))
		compilations.findByName(TEST_FIXTURES_COMPILATION_NAME)?.let {
			compilations.getByName(testSuiteName).associateWith(it)
		}
	}
}

tasks {

	named<Task>("check").configure {
		val integrationTest by existing
		dependsOn(integrationTest)
	}
}
