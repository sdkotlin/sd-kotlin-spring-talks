package org.sdkotlin.springdemo.timelogger

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import org.sdkotlin.springdemo.timeservice.TimeClient
import kotlin.time.Instant

class TimeLogger(
	private val logger: KotlinLogger = TimeLogger::class.logger()
) : TimeClient {

	override fun onTime(instant: Instant) {
		logger.info { "The current time is $instant" }
	}
}
