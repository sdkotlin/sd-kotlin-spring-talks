plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	kotlin("plugin.spring")
}

dependencies {

	modules {
		module("org.springframework.boot:spring-boot-starter-logging") {
			replacedBy("org.springframework.boot:spring-boot-starter-log4j2",
				"Use Log4j2 instead of Logback")
		}
	}

	api(platform("org.sdkotlin.platforms:app-platform"))

	runtimeOnly(kotlin("reflect"))
}
