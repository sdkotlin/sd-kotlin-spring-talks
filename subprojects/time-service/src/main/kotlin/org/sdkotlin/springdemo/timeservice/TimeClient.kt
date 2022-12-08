package org.sdkotlin.springdemo.timeservice

import kotlinx.datetime.Instant

fun interface TimeClient {

	fun onTime(instant: Instant)
}
