plugins {
	id("org.sdkotlin.buildlogic.kotlin-project")
	id("org.sdkotlin.buildlogic.test.integration-test-suite")
	kotlin("plugin.spring")
}

dependencies {

	// Version catalog not available in precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383

	api(platform("org.sdkotlin.platforms:app-platform"))

	//api(platform(libs.spring.boot.bom))
	api(platform("org.springframework.boot:spring-boot-dependencies"))

	runtimeOnly(kotlin("reflect"))

	"integrationTestImplementation"("org.springframework.boot:spring-boot-starter-test")
}
