package org.sdkotlin.springdemo.timeservice

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TimeServerTest {

	@Test
	fun `test time server sends time`() = runTest {

		// Assemble

		val testCoroutineScope: TestScope = this

		val mockTimeClient = MockTimeClient()

		val timeServer = TimeServer(
			timeClients = listOf(mockTimeClient),
			coroutineScope = testCoroutineScope
		)

		// Act

		val job = timeServer.init()

		runCurrent()

		job.cancelAndJoin()

		// Assert

		assertThat(mockTimeClient.receivedInstants).hasSize(1)
	}
}
