package org.sdkotlin.springdemo

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import java.nio.file.Path

@JvmInline
internal value class ConfigPath(val value: Path)

internal class ConfigService(
	private val configPath: ConfigPath,
	private val logger: KotlinLogger = logger(),
) {

	companion object; // Receiver for logger()

	fun init() {
		logger.info { "Config path: ${configPath.value.toAbsolutePath()}" }
	}
}
