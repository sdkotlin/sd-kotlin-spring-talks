import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	// Version catalog type-safe accessors and extension not yet available in
	// plugins block of precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383
	// alias(libs.plugins.dependency.analysis.gradle.plugin)
	id("com.autonomousapps.dependency-analysis")
}

val javaTargetVersion = JavaVersion.VERSION_17.toString()

kotlin {
	jvmToolchain {
		languageVersion.set(JavaLanguageVersion.of(javaTargetVersion))
	}
}

tasks {
	withType<JavaCompile>().configureEach {
		sourceCompatibility = javaTargetVersion
		targetCompatibility = javaTargetVersion
	}

	withType<KotlinCompile>().configureEach {
		kotlinOptions {
			jvmTarget = javaTargetVersion
			freeCompilerArgs = listOf(
				"-Xjsr305=strict",
				"-opt-in=kotlin.ExperimentalStdlibApi",
				"-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
			)
		}
	}

	withType<JavaExec>().configureEach {

		if (name.endsWith("main()")) {

			// https://github.com/gradle/gradle/issues/21364
			notCompatibleWithConfigurationCache("JavaExec created by IntelliJ")
		}
	}
}
