package org.sdkotlin.springdemo.timelogger

import kotlinx.datetime.Instant
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.sdkotlin.springdemo.timeservice.TimeClient

internal class TimeLogger(
	private val logger: Logger = LogManager.getLogger(TimeLogger::class.java)
) : TimeClient {

	override fun onTime(instant: Instant) {
		logger.info("The current time is $instant")
	}
}
