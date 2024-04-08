package org.sdkotlin.springdemo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.nio.charset.StandardCharsets.UTF_8

@SpringBootTest
internal class CustomResourcesTransitiveIT {

	@Configuration
	class TestConfig

	@Test
	@Disabled("Need to make custom resources available on the test classpath.")
	fun `test custom resources can be loaded transitively in tests`(
		@Autowired
		resourceLoader: ResourceLoader
	) {
		val customerResourceContent = resourceLoader
			.getResource("classpath:/custom-resource.txt")
			.getContentAsString(UTF_8)

		assertThat(customerResourceContent).contains("Testing")
	}
}
