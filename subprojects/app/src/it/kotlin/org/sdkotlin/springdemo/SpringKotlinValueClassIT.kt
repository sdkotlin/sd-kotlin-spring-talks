package org.sdkotlin.springdemo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier

@SpringBootTest
internal class SpringKotlinValueClassIT {

	@JvmInline
	value class HelloString(val value: String)

	@JvmInline
	value class WorldString(val value: String)

	@Configuration
	internal class TestConfig {

		@Bean
		fun helloBean(): HelloString =
			HelloString("Hello")

		@Bean
		fun worldBean(): WorldString =
			WorldString("World")

		@Bean
		fun helloFunction(): () -> HelloString =
			{ HelloString("Hello") }

		@Bean
		fun worldFunction(): () -> WorldString =
			{ WorldString("World") }

		@Bean
		fun helloSupplier(): Supplier<HelloString> =
			Supplier { HelloString("Hello") }

		@Bean
		fun worldSupplier(): Supplier<WorldString> =
			Supplier { WorldString("World") }
	}

	@Test
	@Disabled("https://github.com/spring-projects/spring-framework/issues/31372")
	fun `test Kotlin value class beans with Spring`(
		@Autowired
		helloBean: HelloString,
		@Autowired
		worldBean: WorldString,
	) {
		val helloWorld =
			"${helloBean.value}, ${worldBean.value}!"

		assertThat(helloWorld).isEqualTo("Hello, World!")
	}

	@Test
	fun `test Kotlin value class function beans with Spring`(
		@Autowired
		helloFunction: () -> HelloString,
		@Autowired
		worldFunction: () -> WorldString,
	) {
		val helloWorld =
			"${helloFunction().value}, ${worldFunction().value}!"

		assertThat(helloWorld).isEqualTo("Hello, World!")
	}

	@Test
	fun `test Kotlin value class Supplier beans with Spring`(
		@Autowired
		helloSupplier: Supplier<HelloString>,
		@Autowired
		worldSupplier: Supplier<WorldString>,
	) {
		val helloWorld =
			"${helloSupplier.get().value}, ${worldSupplier.get().value}!"

		assertThat(helloWorld).isEqualTo("Hello, World!")
	}
}
