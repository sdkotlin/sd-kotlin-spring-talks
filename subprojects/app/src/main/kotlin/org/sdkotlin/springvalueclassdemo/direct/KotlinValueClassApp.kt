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

class TestService1(
	private val testBean1: TestBean1
) {
	fun init() {
		println(testBean1.value)
	}
}

class TestService2(
	private val testBean2: TestBean2
) {
	fun init() {
		println(testBean2.value)
	}
}

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
	): TestService1 =
		TestService1(testBean1)
			.also { it.init() }

	@Bean
	fun testService2(
		testBean2: TestBean2
	): TestService2 =
		TestService2(testBean2)
			.also { it.init() }
}
