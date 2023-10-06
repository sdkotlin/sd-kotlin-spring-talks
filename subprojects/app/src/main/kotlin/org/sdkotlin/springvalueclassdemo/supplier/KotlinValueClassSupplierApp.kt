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

class TestService1(
	private val testBean1: TestBean1
) {
	fun init() = println(testBean1.value)
}

class TestService2(
	private val testBean2: TestBean2
) {
	fun init() = println(testBean2.value)
}

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
	): TestService1 =
		TestService1(testBean1Supplier())
			.also { it.init() }

	@Bean
	fun testService2(
		testBean2Supplier: () -> TestBean2
	): TestService2 =
		TestService2(testBean2Supplier())
			.also { it.init() }
}
