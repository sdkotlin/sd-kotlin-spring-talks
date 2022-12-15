package org.sdkotlin.springdemo.timelogger.conf

import org.sdkotlin.springdemo.timelogger.TimeLogger
import org.sdkotlin.springdemo.timeservice.TimeClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeLoggerConfiguration {

	@Bean
	internal fun timeLogger(): TimeClient =
		TimeLogger()
}
