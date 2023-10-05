package org.sdkotlin.springdemo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths

@Configuration
internal class AppPathsConfiguration {

	@Bean
	fun configPath(): ConfigPath =
		ConfigPath(Paths.get("config"))

	@Bean
	fun logPath(): LogPath =
		LogPath(Paths.get("logs"))
}
