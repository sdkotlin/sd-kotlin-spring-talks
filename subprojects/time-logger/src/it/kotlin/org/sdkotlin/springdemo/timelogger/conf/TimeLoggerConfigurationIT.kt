// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-23114 is fixed.
@file:Suppress("invisible_reference", "invisible_member")

package org.sdkotlin.springdemo.timelogger.conf

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.timelogger.TimeLogger
import org.sdkotlin.springdemo.timeservice.TimeClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@SpringBootTest
internal class TimeLoggerConfigurationIT {

	@Configuration
	@Import(TimeLoggerConfiguration::class)
	internal class TestConfig

	@Test
	fun `test time logger is in context as TimeClient`(
		@Autowired
		timeLogger: TimeLogger?
	) {
		assertThat(timeLogger).isInstanceOf(TimeClient::class.java)
	}
}
