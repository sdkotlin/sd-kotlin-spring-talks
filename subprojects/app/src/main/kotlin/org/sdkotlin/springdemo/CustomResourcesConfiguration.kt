package org.sdkotlin.springdemo

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.nio.charset.StandardCharsets.UTF_8

@Configuration
internal class CustomResourcesConfiguration {

	/**
	 * Loads and prints the contents of a known custom resource from the
	 * classpath.
	 */
	@Bean
	fun customResourcePrinter(resourceLoader: ResourceLoader) =
		ApplicationRunner {
			val customerResourceContent = resourceLoader
				.getResource("classpath:/custom-resource.txt")
				.getContentAsString(UTF_8)

			println("*** custom-resource.txt context:")
			println(customerResourceContent)
		}
}
