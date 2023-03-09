package org.sdkotlin.springdemo.timelogger

import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Clock
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.junit.jupiter.api.Test

internal class TimeLoggerTest {

	@Test
	fun `test time logging`() {

		// Assemble

		val mockLogger: KotlinLogger = mockk()

		val timeLogger = TimeLogger(
			mockLogger
		)

		val now = Clock.System.now()

		// Act

		timeLogger.onTime(now)

		// Assert

		verify {
			mockLogger.info(match<() -> String> {
				it.invoke().contains(now.toString())
			})
		}
	}
}
