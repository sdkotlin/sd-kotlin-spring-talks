package org.sdkotlin.springdemo.childcontext.rest

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import org.sdkotlin.springdemo.childcontext.rest.ChildContextController.Companion.REQUEST_PATH
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(REQUEST_PATH)
class ChildContextController(
	private val logger: KotlinLogger = logger()
) {

	companion object {
		const val REQUEST_PATH = "/child-context"
	}

	@PutMapping("/{contextId}")
	@ResponseStatus(HttpStatus.CREATED)
	suspend fun createChildContext(
		@PathVariable contextId: String
	) {
		logger.info { """Creating child context: "$contextId".""" }

		// TODO: Use ChildContextService to create child context.
	}
}
