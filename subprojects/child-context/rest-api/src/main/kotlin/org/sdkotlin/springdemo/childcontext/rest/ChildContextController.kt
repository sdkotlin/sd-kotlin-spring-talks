package org.sdkotlin.springdemo.childcontext.rest

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import org.sdkotlin.springdemo.childcontext.domainservice.ChildContextService
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.REQUEST_PATH
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

@RestController
@RequestMapping(REQUEST_PATH)
class ChildContextController(
	private val childContextService: ChildContextService,
	private val logger: KotlinLogger = logger()
) {

	companion object {
		const val REQUEST_PATH = "/child-context"
	}

	@PutMapping("/{childContextId}")
	@ResponseStatus(HttpStatus.CREATED)
	suspend fun createChildContext(
		@PathVariable childContextId: String,
		@RequestBody sourcesFqns: List<String>,
	) {
		logger.info {
			"""Creating child context "$childContextId" for sources "$sourcesFqns"."""
		}

		val sources: List<KClass<*>> = getSourceClasses(sourcesFqns)

		childContextService.createIfAbsent(childContextId, sources)
	}

	private fun getSourceClasses(sourcesFqns: List<String>): List<KClass<*>> =
		sourcesFqns.map { Class.forName(it).kotlin }
}
