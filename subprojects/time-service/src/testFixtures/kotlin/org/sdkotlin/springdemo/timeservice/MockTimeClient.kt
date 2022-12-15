package org.sdkotlin.springdemo.timeservice

import kotlinx.datetime.Instant

class MockTimeClient : TimeClient {

	private val _receivedInstants: MutableList<Instant> =
		mutableListOf()

	val receivedInstants: List<Instant>
		get() = _receivedInstants.toList()

	override fun onTime(instant: Instant) {
		_receivedInstants.add(instant)
	}
}
