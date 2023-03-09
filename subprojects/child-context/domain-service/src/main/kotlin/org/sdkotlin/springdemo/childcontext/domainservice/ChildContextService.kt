package org.sdkotlin.springdemo.childcontext.domainservice

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class ChildContextService(
	private val parentContext: ConfigurableApplicationContext
) {

	companion object {

		const val CHILD_CONTEXT_ID_PROPERTY_NAME: String = "childContextId"
	}

	fun create(
		childContextId: String,
		vararg configuration: KClass<*>,
	): ApplicationContext {

		val classes: Array<Class<out Any>> =
			configuration.map { it.java }.toTypedArray()

		return SpringApplicationBuilder(*classes)
				.parent(parentContext)
				.properties(mapOf(CHILD_CONTEXT_ID_PROPERTY_NAME to childContextId))
				.build()
				.run()
	}
}
