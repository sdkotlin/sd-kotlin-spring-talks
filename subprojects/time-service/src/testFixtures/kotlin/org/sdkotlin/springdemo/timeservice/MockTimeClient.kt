package org.sdkotlin.springdemo.timeservice

import kotlin.time.Instant

class MockTimeClient : TimeClient {

	private val _receivedInstants: MutableList<Instant> =
		mutableListOf()

	val receivedInstants: List<Instant>
		get() = _receivedInstants.toList()

	override fun onTime(instant: Instant) {
		_receivedInstants.add(instant)
	}
}
