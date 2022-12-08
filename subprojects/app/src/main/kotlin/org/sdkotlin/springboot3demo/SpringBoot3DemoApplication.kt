package org.sdkotlin.springboot3demo

import kotlinx.coroutines.CoroutineScope
import org.sdkotlin.springdemo.timeservice.TimeClient
import org.sdkotlin.springdemo.timeservice.conf.TimeServiceConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import kotlin.coroutines.EmptyCoroutineContext

fun main(args: Array<String>) {
	runApplication<SpringBoot3DemoApplication>(*args)
}

@SpringBootApplication
@Import(TimeServiceConfiguration::class)
class SpringBoot3DemoApplication {

	@Bean
	fun applicationCoroutineScope(): CoroutineScope =
		CoroutineScope(EmptyCoroutineContext)

	@Bean
	fun applicationTimeClient(): TimeClient =
		TimeClient { instant ->
			println("Current time is $instant")
		}
}
