package org.sdkotlin.springdemo

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import java.nio.file.Path

@JvmInline
internal value class LogPath(val value: Path)

internal class LogService(
	private val logPath: LogPath,
	private val logger: KotlinLogger = logger(),
) {

	companion object; // Receiver for logger()

	fun init() {
		logger.info { "Log path: ${logPath.value.toAbsolutePath()}" }
	}
}