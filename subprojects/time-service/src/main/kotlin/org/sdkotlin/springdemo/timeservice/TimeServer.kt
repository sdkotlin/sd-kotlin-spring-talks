package org.sdkotlin.springdemo.timeservice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal class TimeServer(
	private val timeClients: List<TimeClient>,
	private val coroutineScope: CoroutineScope,
	private val delayMillis: Duration = 1.seconds
) {
	fun init(): Job =
		coroutineScope.launch {
			while (true) {
				val currentInstant: Instant = Clock.System.now()
				timeClients.forEach { it.onTime(currentInstant) }
				delay(delayMillis)
			}
		}
}
