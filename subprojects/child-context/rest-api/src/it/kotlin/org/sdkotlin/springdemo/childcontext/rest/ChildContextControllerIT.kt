package org.sdkotlin.springdemo.childcontext.rest

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.childcontext.domainservice.ChildContextService
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.LIST_ACTION
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.REQUEST_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.reflect.KClass

@SpringBootTest
@Import(ChildContextController::class, WebTestClientConfig::class)
internal class ChildContextControllerIT(
	@param:Autowired
	private val webClient: WebTestClient,
	@param:Autowired
	private val applicationContext: ApplicationContext,
) {
	@MockkBean
	@Suppress("unused")
	private lateinit var childContextService: ChildContextService

	private fun svc(): ChildContextService =
		applicationContext.getBean<ChildContextService>()

	@Nested
	inner class TestCreate {

		@Test
		fun `test create`() {

			val childContextId = "1"

			val sources: Set<String> = setOf(
				TestChildContextConfig::class.qualifiedName!!,
			)

			webClient.put()
				.uri("$REQUEST_PATH/$childContextId")
				.bodyValue(sources)
				.exchange()
				.expectStatus().isCreated

			verify {
				svc().createIfAbsent(
					childContextId = childContextId,
					sources = setOf(TestChildContextConfig::class)
				)
			}
		}

		@Test
		fun `test create for no sources`() {

			val childContextId = "1"

			webClient.put()
				.uri("$REQUEST_PATH/$childContextId")
				.exchange()
				.expectStatus().isBadRequest

			// TODO Verify validation failure message(s)
		}

		@Test
		fun `test create for service exception`() {

			val childContextId = "1"

			val sources: Set<String> = setOf(
				TestChildContextConfig::class.qualifiedName!!,
			)

			every {
				svc().createIfAbsent(any(), any<Set<KClass<*>>>())
			} throws Exception("Test service exception")

			webClient.put()
				.uri("$REQUEST_PATH/$childContextId")
				.bodyValue(sources)
				.exchange()
				.expectStatus().is5xxServerError
		}
	}

	@Nested
	inner class TestList {

		@Test
		fun `test list`() {

			val childContextId = "1"

			val childContextIds = setOf(childContextId)

			every {
				svc().list()
			} returns childContextIds

			webClient.get()
				.uri("$REQUEST_PATH$LIST_ACTION")
				.exchange()
				.expectStatus().isOk
				.expectBody<Set<String>>().isEqualTo(childContextIds)
		}
	}

	@Nested
	inner class TestRemoveAndClose {

		@Test
		fun `test remove and close`() {

			val childContextId = "1"

			webClient.delete()
				.uri("$REQUEST_PATH/$childContextId")
				.exchange()
				.expectStatus().isOk

			verify {
				svc().removeAndCloseIfPresent(
					childContextId = childContextId,
				)
			}
		}
	}
}

// Defined outside the test class so as not to contribute configuration to the
// test itself, except for explicit imports.

@Configuration
internal class TestChildContextConfig {

	@Bean
	fun testBean() = TestBean()
}

@TestConfiguration
internal class WebTestClientConfig {

	@Bean
	fun webTestClient(childContextService: ChildContextService): WebTestClient =
		WebTestClient.bindToController(
			ChildContextController(childContextService)
		).configureClient().build()
}

internal class TestBean
