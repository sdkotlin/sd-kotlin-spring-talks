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

		internal const val NO_SOURCES_MESSAGE: String =
			"One or more sources is required."
	}

	private val childContextsMap: ConcurrentMap<String, ApplicationContext> =
		ConcurrentHashMap()

	fun createIfAbsent(
		childContextId: String,
		vararg sources: KClass<*>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit = {},
	): ApplicationContext {

		require(sources.isNotEmpty()) { NO_SOURCES_MESSAGE }

		return childContextsMap.computeIfAbsent(childContextId) {

			val sourceJavaClasses: Array<Class<out Any>> =
				sources.map { it.java }.toTypedArray()

			val springApplicationBuilder =
				SpringApplicationBuilder(*sourceJavaClasses)
						.parent(parentContext)
						.properties(mapOf(CHILD_CONTEXT_ID_PROPERTY_NAME to childContextId))

			springApplicationBuilderConfigurer(springApplicationBuilder)

			springApplicationBuilder
					.build()
					.run()
		}
	}

	fun get(childContextId: String): ApplicationContext? =
		childContextsMap[childContextId]
}
