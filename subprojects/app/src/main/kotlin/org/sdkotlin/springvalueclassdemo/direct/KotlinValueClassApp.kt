package org.sdkotlin.springvalueclassdemo.direct

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
	runApplication<KotlinValueClassApp>(*args)
}

@JvmInline
value class TestBean1(val value: String)

@JvmInline
value class TestBean2(val value: String)

@SpringBootApplication
class KotlinValueClassApp {

	@Bean
	fun testBean1(): TestBean1 =
		TestBean1("Test 1")

	@Bean
	fun testBean2(): TestBean2 =
		TestBean2("Test 2")

	@Bean
	fun testService1(
		testBean1: TestBean1
	): String =
		testBean1.value
			.also { println("Test bean injected: $it") }

	@Bean
	fun testService2(
		testBean2: TestBean2
	): String =
		testBean2.value
			.also { println("Test bean injected: $it") }
}
