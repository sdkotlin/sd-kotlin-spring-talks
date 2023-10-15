package org.sdkotlin.springdemo

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Test case for
 * [spring-projects/spring-framework#31439](https://github.com/spring-projects/spring-framework/issues/31439).
 *
 * Passes with:
 *
 * ```kotlin
 * inline fun <reified T : Any> BeanFactory.getBean(): T =
 * 	getBeanProvider<T>(
 * 		ResolvableType.forType(
 * 			(object : ParameterizedTypeReference<T>() {}).type
 * 		)
 * 	).`object`
 * ```
 */
@SpringBootTest
internal class SpringGetBeanNestedGenericIT {

	companion object {

		val LIST_OF_STRING: List<String> =
			listOf("Testing")

		val LIST_OF_LIST_OF_STRING: List<List<String>> =
			listOf(listOf("Testing"))
	}

	@Configuration
	class TestConfig {

		@Bean
		fun listOfString(): List<String> =
			LIST_OF_STRING

		@Bean
		fun listOfListOfString(): List<List<String>> =
			LIST_OF_LIST_OF_STRING
	}

	@Test
	@Disabled
	fun `test getBean with nested generic`(
		applicationContext: ApplicationContext
	) {
		assertThatNoException().isThrownBy {

			applicationContext.getBean<List<String>>()
		}
	}

	@Test
	fun `test listOfString is injected`(
		@Autowired
		listOfString: List<String>
	) {
		assertThat(listOfString).isEqualTo(LIST_OF_STRING)
	}

	@Test
	fun `test listOfListOfString is injected`(
		@Autowired
		listOfListOfString: List<List<String>>
	) {
		assertThat(listOfListOfString).isEqualTo(LIST_OF_LIST_OF_STRING)
	}
}
