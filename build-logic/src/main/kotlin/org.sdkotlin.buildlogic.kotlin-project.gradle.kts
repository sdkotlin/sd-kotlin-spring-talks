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

val versionCatalog = versionCatalogs.named("libs")
val javaTargetVersion = versionCatalog.findVersion("jvm").get().requiredVersion.toInt()

jvmDependencyConflicts {
	logging {
		enforceLog4J2()
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(javaTargetVersion))
	}
}

kotlin {
	jvmToolchain(javaTargetVersion)
}

tasks {

	withType<JavaCompile>().configureEach {

		with(options) {
			release = javaTargetVersion
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
