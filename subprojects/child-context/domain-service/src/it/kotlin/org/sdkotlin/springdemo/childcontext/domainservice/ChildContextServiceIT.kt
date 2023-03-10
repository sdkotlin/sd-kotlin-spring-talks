package org.sdkotlin.springdemo.childcontext.domainservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.childcontext.domainservice.ChildContextService.Companion.CHILD_CONTEXT_ID_PROPERTY_NAME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.get
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD

@SpringBootTest
// ChildContextService is mutable
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
internal class ChildContextServiceIT(
	@Autowired
	private val parentContext: ConfigurableApplicationContext,
	@Autowired
	private val childContextService: ChildContextService
) {

	@Configuration
	internal class TestConfig {

		@Bean
		fun childContextService(
			applicationContext: ConfigurableApplicationContext
		) = ChildContextService(applicationContext)
	}

	@Test
	fun `test create`() {

		val testId = "testing"

		val childContext: ApplicationContext =
			childContextService.createIfAbsent(
				childContextId = testId,
				TestChildContextConfig::class,
			)

		val beanName = TestChildContextConfig::testBean.name

		val parentContextContainsBean = parentContext.containsBean(beanName)
		val childContextContainsBean = childContext.containsBean(beanName)

		assertThat(parentContextContainsBean)
				.describedAs("parent context contains $beanName")
				.isFalse

		assertThat(childContextContainsBean)
				.describedAs("child context contains $beanName")
				.isTrue

		val childContextId =
			childContext.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

		assertThat(childContextId)
				.describedAs("childContextId")
				.isEqualTo(testId)
	}

	@Test
	fun `test create for multiple child contexts`() {

		val testId1 = "testing 1"
		val testId2 = "testing 2"

		val childContext1: ApplicationContext =
			childContextService.createIfAbsent(
				childContextId = testId1,
				TestChildContextConfig::class,
			)

		val childContext2: ApplicationContext =
			childContextService.createIfAbsent(
				childContextId = testId2,
				TestChildContextConfig::class,
			)

		val beanName = TestChildContextConfig::testBean.name

		val parentContextContainsBean = parentContext.containsBean(beanName)
		val childContext1ContainsBean = childContext1.containsBean(beanName)
		val childContext2ContainsBean = childContext2.containsBean(beanName)

		assertThat(parentContextContainsBean)
				.describedAs("parent context contains $beanName")
				.isFalse

		assertThat(childContext1ContainsBean)
				.describedAs("child context 1 contains $beanName")
				.isTrue

		assertThat(childContext2ContainsBean)
				.describedAs("child context 2 contains $beanName")
				.isTrue

		val childContext1Id =
			childContext1.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

		val childContext2Id =
			childContext2.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

		assertThat(childContext1Id)
				.describedAs("childContext1Id")
				.isEqualTo(testId1)

		assertThat(childContext2Id)
				.describedAs("childContext2Id")
				.isEqualTo(testId2)
	}

	@Test
	fun `test create for multiple configuration sources`() {

		val testId = "testing"

		val childContext: ApplicationContext =
			childContextService.createIfAbsent(
				testId,
				TestChildContextConfig::class,
				TestChildContextConfig2::class,
			)

		val beanName = TestChildContextConfig::testBean.name
		val beanName2 = TestChildContextConfig2::testBean2.name

		val childContextContainsBean = childContext.containsBean(beanName)
		val childContextContainsBean2 = childContext.containsBean(beanName2)

		assertThat(childContextContainsBean)
				.describedAs("child context contains $beanName")
				.isTrue

		assertThat(childContextContainsBean2)
				.describedAs("child context contains $beanName2")
				.isTrue
	}

	@Test
	fun `test create for idempotency`() {

		val testId = "testing"

		childContextService.createIfAbsent(
			childContextId = testId,
			InitializationCountingTestChildContextConfig::class,
		)

		childContextService.createIfAbsent(
			childContextId = testId,
			InitializationCountingTestChildContextConfig::class,
		)

		assertThat(
			InitializationCountingTestChildContextConfig.initializationCount
		).isEqualTo(1)
	}
}

// Defined private and outside the test class so as not to contribute
// configuration to the test itself, which is the parent context for the test.

@Configuration
private class TestChildContextConfig {

	@Bean
	fun testBean() = TestBean()
}

@Configuration
private class TestChildContextConfig2 {

	@Bean
	fun testBean2() = TestBean()
}

@Configuration
private class InitializationCountingTestChildContextConfig {

	companion object {

		val initializationCount: Int
			get() = _initializationCount

		private var _initializationCount: Int = 0
	}

	@Bean
	fun testBean(): TestBean {
		_initializationCount++
		return TestBean()
	}
}

private class TestBean
