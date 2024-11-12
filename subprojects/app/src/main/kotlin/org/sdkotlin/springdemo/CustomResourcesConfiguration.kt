package org.sdkotlin.springdemo

import org.apache.logging.log4j.kotlin.logger
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.nio.charset.StandardCharsets.UTF_8

@Configuration
internal class CustomResourcesConfiguration {

	companion object {
		const val CUSTOM_RESOURCE_FILENAME = "custom-resource.txt"
	}

	/**
	 * Loads and prints the contents of a known custom resource from the
	 * classpath.
	 */
	@Bean
	fun customResourcePrinter(
		resourceLoader: ResourceLoader
	): ApplicationRunner =
		ApplicationRunner {
			val customerResourceContent = resourceLoader
				.getResource("classpath:/$CUSTOM_RESOURCE_FILENAME")
				.getContentAsString(UTF_8)

			logger.info {
				"$CUSTOM_RESOURCE_FILENAME content:\n$customerResourceContent"
			}
		}
}
