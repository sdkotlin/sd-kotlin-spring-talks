import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	// Version catalog type-safe accessors and extension not yet available in
	// plugins block of precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383
	// alias(libs.plugins.dependencyAnalysis.gradlePlugin)
	id("com.autonomousapps.dependency-analysis")
	// alias(libs.plugins.jvmDependencyConflictDetection.gradlePlugin)
	id("org.gradlex.jvm-dependency-conflict-detection")
	// alias(libs.plugins.jvmDependencyConflictResolution.gradlePlugin)
	id("org.gradlex.jvm-dependency-conflict-resolution")
}

val javaTargetVersion: String = JavaVersion.VERSION_21.toString()

jvmDependencyConflicts {
	logging {
		enforceLog4J2()
	}
}

tasks {

	withType<JavaCompile>().configureEach {

		with(options) {
			release = javaTargetVersion.toInt()
			isFork = true
		}
	}

	withType<KotlinCompile>().configureEach {
		compilerOptions {
			optIn = listOf(
				"kotlin.ExperimentalStdlibApi",
				"kotlin.contracts.ExperimentalContracts",
				"kotlin.time.ExperimentalTime",
			)
			// Planned for deprecation:
			// https://youtrack.jetbrains.com/issue/KT-61035/
			freeCompilerArgs = listOf(
				// https://youtrack.jetbrains.com/issue/KT-61410/
				"-Xjsr305=strict",
				// https://youtrack.jetbrains.com/issue/KT-49746/
				"-Xjdk-release=$javaTargetVersion"
			)
		}
	}
}
