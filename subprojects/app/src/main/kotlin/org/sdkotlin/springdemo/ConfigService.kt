package org.sdkotlin.springdemo

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import java.nio.file.Path

@JvmInline
value class ConfigPath(val value: Path)

internal class ConfigService(
	private val configPathSupplier: () -> ConfigPath,
	private val logger: KotlinLogger = ConfigService::class.logger(),
) {
	fun init() {
		logger.info {
			"Config path: ${configPathSupplier().value.toAbsolutePath()}"
		}
	}
}
