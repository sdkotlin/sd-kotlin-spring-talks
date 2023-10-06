package org.sdkotlin.springvalueclassdemo.supplier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
	runApplication<KotlinValueClassSupplierApp>(*args)
}

@JvmInline
value class TestBean1(val value: String)

@JvmInline
value class TestBean2(val value: String)

@SpringBootApplication
class KotlinValueClassSupplierApp {

	@Bean
	fun testBean1Supplier(): () -> TestBean1 =
		{ TestBean1("Test 1") }

	@Bean
	fun testBean2Supplier(): () -> TestBean2 =
		{ TestBean2("Test 2") }

	@Bean
	fun testService1(
		testBean1Supplier: () -> TestBean1
	): String =
		testBean1Supplier().value
			.also { println("Test bean injected: $it") }

	@Bean
	fun testService2(
		testBean2Supplier: () -> TestBean2
	): String =
		testBean2Supplier().value
			.also { println("Test bean injected: $it") }
}
