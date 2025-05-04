package org.sdkotlin.springdemo.childcontext.domainservice

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

@Service
class ConcurrentMapChildContextService(
	private val parentContext: ConfigurableApplicationContext
) : ChildContextService {

	companion object {

		const val CHILD_CONTEXT_ID_PROPERTY_NAME: String = "childContextId"

		internal const val NO_SOURCES_MESSAGE: String =
			"One or more sources is required."
	}

	private val childContextsMap:
		ConcurrentMap<String, ConfigurableApplicationContext> =
		ConcurrentHashMap()

	override fun createIfAbsent(
		childContextId: String,
		source: KClass<*>,
		springApplicationBuilderConfigurer: (SpringApplicationBuilder) -> Unit
	): ConfigurableApplicationContext =
		createIfAbsent(
			childContextId,
			setOf(source),
			springApplicationBuilderConfigurer
		)

	override fun createIfAbsent(
		childContextId: String,
		sources: Set<KClass<*>>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit,
	): ConfigurableApplicationContext {

		require(sources.isNotEmpty()) { NO_SOURCES_MESSAGE }

		return childContextsMap.computeIfAbsent(childContextId) {

			val sourceJavaClasses: Array<Class<out Any>> =
				sources.map { it.java }.toTypedArray()

			val springApplicationBuilder =
				SpringApplicationBuilder(*sourceJavaClasses)
					.parent(parentContext)
					.properties(
						mapOf(CHILD_CONTEXT_ID_PROPERTY_NAME to childContextId)
					)

			springApplicationBuilderConfigurer(springApplicationBuilder)

			springApplicationBuilder
				.build()
				.run()
		}
	}

	override fun list(): Set<String> =
		childContextsMap.keys

	override fun get(childContextId: String): ConfigurableApplicationContext? =
		childContextsMap[childContextId]

	override fun removeAndCloseIfPresent(childContextId: String):
		ConfigurableApplicationContext? {

		val applicationContext = childContextsMap.remove(childContextId)

		applicationContext?.close()

		return applicationContext
	}
}
