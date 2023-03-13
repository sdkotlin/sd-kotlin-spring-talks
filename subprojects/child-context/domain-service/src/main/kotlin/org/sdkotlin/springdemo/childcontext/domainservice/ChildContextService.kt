package org.sdkotlin.springdemo.childcontext.domainservice

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import kotlin.reflect.KClass

interface ChildContextService {

	fun createIfAbsent(
		childContextId: String,
		vararg sources: KClass<*>,
		springApplicationBuilderConfigurer:
			(SpringApplicationBuilder) -> Unit = {},
	): ConfigurableApplicationContext

	fun get(childContextId: String): ConfigurableApplicationContext?

	fun removeAndCloseIfPresent(childContextId: String):
			ConfigurableApplicationContext?
}
