package org.sdkotlin.springdemo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SpringBootAppIT {

	@Test
	fun `test context loads`() {
		// Will fail if Spring application context loading throws an exception.
	}
}
