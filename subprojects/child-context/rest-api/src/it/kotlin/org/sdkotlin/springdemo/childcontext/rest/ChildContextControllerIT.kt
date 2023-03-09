// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-7662 is fixed.
@file:Suppress("invisible_reference", "invisible_member")

package org.sdkotlin.springdemo.childcontext.rest

import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.REQUEST_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(ChildContextController::class)
class ChildContextControllerIT(
	@Autowired
	val webClient: WebTestClient
) {

	@Test
	fun `test child context creation`() {

		val contextId = 1

		webClient.put()
				.uri("$REQUEST_PATH/$contextId")
				.exchange()
				.expectStatus().isCreated
	}
}
