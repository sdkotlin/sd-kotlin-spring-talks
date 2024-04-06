package org.sdkotlin.springdemo

import kotlinx.coroutines.CoroutineScope
import org.sdkotlin.springdemo.componentscannedservice.ComponentScannedService
import org.sdkotlin.springdemo.timelogger.conf.TimeLoggerConfiguration
import org.sdkotlin.springdemo.timeservice.conf.TimeServiceConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import kotlin.coroutines.EmptyCoroutineContext

fun main(args: Array<String>) {
	runApplication<SpringBootApp>(*args)
}

@SpringBootApplication
@Import(
	AppPathsConfiguration::class,
	CustomResourcesConfiguration::class,
	TimeLoggerConfiguration::class,
	TimeServiceConfiguration::class,
)
internal class SpringBootApp {

	@Bean
	fun configService(
		configPathSupplier: () -> ConfigPath
	) = ConfigService(configPathSupplier).also { it.init() }

	@Bean
	fun logService(
		logPathSupplier: () -> LogPath
	) = LogService(logPathSupplier).also { it.init() }

	@Bean
	fun applicationServicesCoroutineScope(): CoroutineScope =
		CoroutineScope(EmptyCoroutineContext)

	@Bean
	fun componentScannedServiceClient(
		componentScannedService: ComponentScannedService
	): String =
		componentScannedService.doIt()
			.also { println(it) }
}
