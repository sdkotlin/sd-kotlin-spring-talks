import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.gradle.buildconfiguration.tasks.UpdateDaemonJvm
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec

plugins {
	id("base")
	alias(libs.plugins.dependencyAnalysis.gradlePlugin)
	// Declare with `apply false` to include in Versions Gradle Plugin checks.
	alias(libs.plugins.jvmDependencyConflictDetection.gradlePlugin) apply false
	// Declare with `apply false` to include in Versions Gradle Plugin checks.
	alias(libs.plugins.jvmDependencyConflictResolution.gradlePlugin) apply false
	// Kotlin plugin declaration needed here for the Dependency Analysis Plugin,
	// but with `apply false` since the root project itself isn't a Kotlin
	// project:
	// https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/FAQ#typenotpresentexception-type-orgjetbrainskotlingradledslkotlinprojectextension-in-kotlin-jvm-library
	alias(libs.plugins.kotlin.gradlePlugin) apply false
	// Declare with `apply false` to include in Versions Gradle Plugin checks.
	alias(libs.plugins.springboot.gradlePlugin) apply false
	// Declare with `apply false` to include in Versions Gradle Plugin checks.
	alias(libs.plugins.osdetector.gradlePlugin) apply false
}

// Each included build produces its own partial results, and the entries below
// merge them into this report. This task's own settings are then applied to
// everything merged, so the included builds need no task configuration.
dependencies {
	dependencyUpdatesAggregation("org.sdkotlin.buildlogic:build-logic:1.0.0-SNAPSHOT")
	dependencyUpdatesAggregation("org.sdkotlin.platforms:platforms:1.0.0-SNAPSHOT")
}

dependencyAnalysis {
	issues {
		all {
			onAny {
				severity("fail")
			}
			onUnusedDependencies {
				exclude(
					// Test dependencies added globally for convenience.
					"io.mockk:mockk-dsl-jvm",
					"org.assertj:assertj-core",
					"org.jetbrains.kotlin:kotlin-stdlib",
					"org.junit.jupiter:junit-jupiter",
					"org.junit.jupiter:junit-jupiter-api",
					"org.junit.jupiter:junit-jupiter-params",
				)
			}
			onIncorrectConfiguration {
				// https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/1059
				severity("fail")
				exclude("org.jetbrains.kotlin:kotlin-stdlib")
			}
		}
	}
	usage {
		analysis {
			checkSuperClasses(true)
		}
	}
	useTypesafeProjectAccessors(true)
}

tasks {
	named<DependencyUpdatesTask>("dependencyUpdates").configure {
		checkConstraints = true
		// `filterConfigurations` would skip the lookups too, but it covers only
		// what this build resolves, leaving the KGP internal classpath rows in
		// the entries merged from the included builds. The predicate is inlined
		// because a rule that calls a function declared in this script cannot be
		// stored in the configuration cache once entries are merged.
		filterDeclaredConfigurations = Spec { configurationName ->
			val kgpInternalConfigurations = setOf(
				"kotlinAbiValidationCompatClasspath",
				"kotlinBouncyCastleConfiguration",
				"kotlinBuildToolsApiClasspath",
				"kotlinCompilerClasspath",
				"kotlinKlibCommonizerClasspath",
			)
			configurationName != "dependencyAnalysisKotlinMetadata" &&
				configurationName !in kgpInternalConfigurations &&
				!(configurationName.startsWith("kotlinCompilerPluginClasspath") &&
					configurationName != "kotlinCompilerPluginClasspath")
		}
		// Replaces the former `isNonStable` recipe. `rejectOutOfBounds` covers
		// the former `!satisfiesDeclaredBound` clause and is on by default, as
		// is the `current` Gradle release channel once pre-releases are out.
		rejectPreReleases = true
	}

	named<Wrapper>("wrapper").configure {
		gradleVersion = "9.7.0"
		distributionType = ALL
	}

	named<UpdateDaemonJvm>("updateDaemonJvm").configure {
		// Resolving the per-platform toolchain download URLs is not compatible
		// with the configuration cache. This task only runs by hand during a
		// Java upgrade, so opt it out rather than caching it.
		notCompatibleWithConfigurationCache(
			"updateDaemonJvm resolves toolchain download URLs at execution time."
		)
		languageVersion =
			JavaLanguageVersion.of(libs.versions.java.get().toInt())
		vendor = JvmVendorSpec.ADOPTIUM
	}
}

