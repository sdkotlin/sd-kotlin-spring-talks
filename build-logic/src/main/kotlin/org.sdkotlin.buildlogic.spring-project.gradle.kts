plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	kotlin("plugin.spring")
}

// Version catalog type-safe accessors not available in precompiled script
// plugins: https://github.com/gradle/gradle/issues/15383. Use the version
// catalog extension instead.
val versionCatalog = versionCatalogs.named("libs")

dependencies {

	modules {
		module("org.springframework.boot:spring-boot-starter-logging") {
			replacedBy("org.springframework.boot:spring-boot-starter-log4j2",
				"Use Log4j2 instead of Logback")
		}
	}

	api(platform("org.sdkotlin.platforms:app-platform"))

	runtimeOnly(
		versionCatalog.findLibrary("springboot-starter-log4j2").get()
	)
}
