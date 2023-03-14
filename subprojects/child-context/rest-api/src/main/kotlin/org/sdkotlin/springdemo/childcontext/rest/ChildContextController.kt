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

		const val LIST_ACTION = "/list"
	}

	@PutMapping("/{childContextId}")
	@ResponseStatus(HttpStatus.CREATED)
	suspend fun createChildContext(
		@PathVariable childContextId: String,
		@RequestBody sourcesFqns: Set<String>,
	) {
		logger.info {
			"""Creating child context "$childContextId" for sources "$sourcesFqns"."""
		}

		val sources: Set<KClass<*>> = getSourceClasses(sourcesFqns)

		childContextService.createIfAbsent(childContextId, sources)
	}

	@GetMapping(LIST_ACTION)
	@ResponseStatus(HttpStatus.OK)
	suspend fun listChildContextIds(): Set<String> {

		logger.info {
			"""Listing child context IDs."""
		}

		return childContextService.list()
	}

	@DeleteMapping("/{childContextId}")
	@ResponseStatus(HttpStatus.OK)
	suspend fun removeAndCloseChildContext(
		@PathVariable childContextId: String,
	) {

		logger.info {
			"""Removing and closing child context "$childContextId"."""
		}

		childContextService.removeAndCloseIfPresent(childContextId)
	}

	private fun getSourceClasses(sourcesFqns: Set<String>): Set<KClass<*>> =
		sourcesFqns.map { Class.forName(it).kotlin }.toSet()
}
