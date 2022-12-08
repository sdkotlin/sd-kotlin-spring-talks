package org.sdkotlin.springdemo.timeservice.conf

import kotlinx.coroutines.CoroutineScope
import org.sdkotlin.springdemo.timeservice.TimeClient
import org.sdkotlin.springdemo.timeservice.TimeServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeServiceConfiguration {

	@Bean
	internal fun timeServer(
		timeClients: List<TimeClient>,
		applicationCoroutineScope: CoroutineScope,
	) = TimeServer(timeClients, applicationCoroutineScope)
		.apply { init() }
}
