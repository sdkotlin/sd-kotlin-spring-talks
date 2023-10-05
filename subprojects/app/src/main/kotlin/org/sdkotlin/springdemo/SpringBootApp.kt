package org.sdkotlin.springdemo

import jakarta.inject.Named
import kotlinx.coroutines.CoroutineScope
import org.sdkotlin.springdemo.AppPathsConfiguration.Companion.CONFIG_PATH_BEAN_NAME
import org.sdkotlin.springdemo.timelogger.conf.TimeLoggerConfiguration
import org.sdkotlin.springdemo.timeservice.conf.TimeServiceConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import java.nio.file.Path
import kotlin.coroutines.EmptyCoroutineContext

fun main(args: Array<String>) {
	runApplication<SpringBootApp>(*args)
}

@SpringBootApplication
@Import(
	AppPathsConfiguration::class,
	TimeLoggerConfiguration::class,
	TimeServiceConfiguration::class,
)
internal class SpringBootApp {

	@Bean
	fun configService(
		@Named(CONFIG_PATH_BEAN_NAME)
		configPath: Path
	) = ConfigService(configPath).also { it.init() }

	@Bean
	fun logService(
		@Named(CONFIG_PATH_BEAN_NAME)
		logPath: Path
	) = LogService(logPath).also { it.init() }

	@Bean
	fun applicationServicesCoroutineScope(): CoroutineScope =
		CoroutineScope(EmptyCoroutineContext)
}
