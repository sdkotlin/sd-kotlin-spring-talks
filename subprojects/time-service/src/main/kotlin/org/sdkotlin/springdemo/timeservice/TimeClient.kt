package org.sdkotlin.springdemo.timeservice

import kotlin.time.Instant


fun interface TimeClient {

	fun onTime(instant: Instant)
}
