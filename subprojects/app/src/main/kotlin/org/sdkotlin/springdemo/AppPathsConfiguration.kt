package org.sdkotlin.springdemo

import jakarta.inject.Named
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
internal class AppPathsConfiguration {

	companion object {

		const val CONFIG_PATH_BEAN_NAME = "configPath"

		const val LOG_PATH_BEAN_NAME = "logPath"
	}

	@Bean
	@Named(CONFIG_PATH_BEAN_NAME)
	fun configPath(): Path = Paths.get("config")

	@Bean
	@Named(LOG_PATH_BEAN_NAME)
	fun logPath(): Path = Paths.get("logs")
}
