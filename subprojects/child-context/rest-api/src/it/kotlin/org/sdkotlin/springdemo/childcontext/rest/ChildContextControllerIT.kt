// TODO: Remove when https://youtrack.jetbrains.com/issue/KTIJ-23114 is fixed.
@file:Suppress("invisible_reference", "invisible_member")

package org.sdkotlin.springdemo.childcontext.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.childcontext.domainservice.ChildContextService
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.REQUEST_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(ChildContextController::class)
class ChildContextControllerIT(
	@Autowired
	val webClient: WebTestClient
) {

	@MockkBean
	private lateinit var childContextService: ChildContextService

	@Test
	fun `test child context creation`() {

		val childContextId = "1"

		val sources: List<String> = listOf(
			TestChildContextConfig::class.qualifiedName!!,
		)

		webClient.put()
				.uri("$REQUEST_PATH/$childContextId")
				.bodyValue(sources)
				.exchange()
				.expectStatus().isCreated

		verify {
			childContextService.createIfAbsent(
				childContextId = childContextId,
				sources = listOf(TestChildContextConfig::class)
			)
		}
	}
}

@Configuration
class TestChildContextConfig {

	@Bean
	fun testBean() = TestBean()
}

class TestBean
