package org.sdkotlin.springdemo.timeservice.conf

import kotlinx.coroutines.CoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.timeservice.TimeServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import kotlin.coroutines.EmptyCoroutineContext

@SpringBootTest
internal class TimeServiceConfigurationIT {

	@Configuration
	@Import(TimeServiceConfiguration::class)
	class TestConfig {

		@Bean
		fun applicationCoroutineScope(): CoroutineScope =
			CoroutineScope(EmptyCoroutineContext)
	}

	@Test
	fun `test time server is in context`(
		@Autowired
		timeServer: TimeServer?
	) {
		assertThat(timeServer)
			.describedAs("timeServer")
			.isNotNull
	}
}
