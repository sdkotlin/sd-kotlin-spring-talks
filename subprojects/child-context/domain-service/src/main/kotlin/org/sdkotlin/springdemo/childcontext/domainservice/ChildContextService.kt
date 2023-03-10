package org.sdkotlin.springdemo.childcontext.domainservice

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

@Service
class ChildContextService(
	private val parentContext: ConfigurableApplicationContext
) {

	companion object {

		const val CHILD_CONTEXT_ID_PROPERTY_NAME: String = "childContextId"
	}

	private val childContextsMap: ConcurrentMap<String, ApplicationContext> =
		ConcurrentHashMap()

	fun createIfAbsent(
		childContextId: String,
		vararg configuration: KClass<*>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit = {},
	): ApplicationContext {

		return childContextsMap.computeIfAbsent(childContextId) {

			val classes: Array<Class<out Any>> =
				configuration.map { it.java }.toTypedArray()

			val springApplicationBuilder = SpringApplicationBuilder(*classes)
					.parent(parentContext)
					.properties(mapOf(CHILD_CONTEXT_ID_PROPERTY_NAME to childContextId))

			springApplicationBuilderConfigurer(springApplicationBuilder)

			springApplicationBuilder
					.build()
					.run()
		}
	}
}
