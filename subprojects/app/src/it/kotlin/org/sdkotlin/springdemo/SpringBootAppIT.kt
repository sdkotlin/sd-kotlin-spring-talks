package org.sdkotlin.springdemo

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Disabled("Need to make custom resources available on the test classpath.")
internal class SpringBootAppIT {

	@Test
	fun `test context loads`() {
		// Will fail if Spring application context loading throws an exception.
	}
}
