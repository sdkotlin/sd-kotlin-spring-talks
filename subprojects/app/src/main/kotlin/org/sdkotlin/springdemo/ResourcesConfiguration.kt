package org.sdkotlin.springdemo

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
internal class ResourcesConfiguration {

	/**
	 * Loads and prints the contents of a known custom resource from the
	 * classpath.
	 */
	@Bean
	fun customResourcePrinter(
		resourceLoader: ResourceLoader
	): ApplicationRunner =
		ResourcePrinter(resourceLoader, "classpath:/custom-resource.txt")

	/**
	 * Loads and prints the contents of a known native resource from the
	 * classpath.
	 */
	@Bean
	fun nativeResourcePrinter(
		resourceLoader: ResourceLoader
	): ApplicationRunner =
		ResourcePrinter(resourceLoader, "classpath:/native-resource.txt")
}
