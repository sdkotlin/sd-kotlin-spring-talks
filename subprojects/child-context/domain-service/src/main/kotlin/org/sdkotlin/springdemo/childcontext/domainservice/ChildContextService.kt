package org.sdkotlin.springdemo.childcontext.domainservice

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import kotlin.reflect.KClass

interface ChildContextService {

	fun createIfAbsent(
		childContextId: String,
		source: KClass<*>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit = {},
	): ConfigurableApplicationContext

	fun createIfAbsent(
		childContextId: String,
		sources: Set<KClass<*>>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit = {},
	): ConfigurableApplicationContext

	fun list(): Set<String>

	fun get(childContextId: String): ConfigurableApplicationContext?

	fun removeAndCloseIfPresent(childContextId: String):
		ConfigurableApplicationContext?
}
