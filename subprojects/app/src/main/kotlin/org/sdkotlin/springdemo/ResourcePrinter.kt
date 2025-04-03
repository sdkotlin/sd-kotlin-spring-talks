package org.sdkotlin.springdemo

import org.apache.logging.log4j.kotlin.logger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ResourceLoader
import java.nio.charset.StandardCharsets.UTF_8

class ResourcePrinter(
	private val resourceLoader: ResourceLoader,
	private val resourceLocation: String,
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {

		val resourceContent = resourceLoader
			.getResource(resourceLocation)
			.getContentAsString(UTF_8)

		logger.info {
			"$resourceLocation content:\n$resourceContent"
		}
	}
}
