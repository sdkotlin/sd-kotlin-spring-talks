package org.sdkotlin.springdemo.childcontext.domainservice

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.sdkotlin.springdemo.childcontext.domainservice.ConcurrentMapChildContextService.Companion.CHILD_CONTEXT_ID_PROPERTY_NAME
import org.sdkotlin.springdemo.childcontext.domainservice.ConcurrentMapChildContextService.Companion.NO_SOURCES_MESSAGE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
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
internal class ConcurrentMapChildContextServiceIT(
	@Autowired
	private val parentContext: ConfigurableApplicationContext,
	@Autowired
	private val childContextService: ChildContextService
) {
	@SpringBootConfiguration
	class TestConfig {

		@Bean
		fun childContextService(
			applicationContext: ConfigurableApplicationContext
		) = ConcurrentMapChildContextService(applicationContext)
	}

	@Nested
	inner class TestCreate {

		@Test
		fun `test create for doesn't exist`() {

			val childContextId = "testing"

			val childContext: ApplicationContext =
				childContextService.createIfAbsent(
					childContextId,
					source = TestChildContextConfig::class,
				)

			val testBeanName = TestChildContextConfig::testBean.name

			val parentContextContainsBean =
				parentContext.containsBean(testBeanName)
			val childContextContainsBean =
				childContext.containsBean(testBeanName)

			assertThat(parentContextContainsBean)
				.describedAs("parent context contains $testBeanName")
				.isFalse

			assertThat(childContextContainsBean)
				.describedAs("child context contains $testBeanName")
				.isTrue

			val childContextIdFromEnvironment =
				childContext.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

			assertThat(childContextIdFromEnvironment)
				.describedAs("childContextId from environment")
				.isEqualTo(childContextId)
		}

		@Test
		fun `test create for does exist`() {

			val sameChildContextId = "testing"

			childContextService.createIfAbsent(
				childContextId = sameChildContextId,
				source = InitializationCountingTestChildContextConfig::class,
			)

			childContextService.createIfAbsent(
				childContextId = sameChildContextId,
				source = InitializationCountingTestChildContextConfig::class,
			)

			assertThat(
				InitializationCountingTestChildContextConfig.initializationCount
			).isEqualTo(1)
		}

		@Test
		fun `test create for additional SpringBootApplication configuration`() {

			val childContextId = "testing"

			val additionalPropertyKey = "additionalProperty"
			val additionalPropertyValue = "additionalPropertyValue"

			val childContext: ApplicationContext =
				childContextService.createIfAbsent(
					childContextId,
					source = TestChildContextConfig::class,
				) { springApplicationBuilder ->
					springApplicationBuilder.properties(
						mapOf(additionalPropertyKey to additionalPropertyValue)
					)
				}

			val additionalPropertyFromEnvironment =
				childContext.environment[additionalPropertyKey]

			assertThat(additionalPropertyFromEnvironment)
				.describedAs("additionalProperty from environment")
				.isEqualTo(additionalPropertyValue)
		}

		@Test
		fun `test create for multiple child contexts`() {

			val childContextId1 = "testing 1"
			val childContextId2 = "testing 2"

			val childContext1: ApplicationContext =
				childContextService.createIfAbsent(
					childContextId1,
					source = TestChildContextConfig::class,
				)

			val childContext2: ApplicationContext =
				childContextService.createIfAbsent(
					childContextId2,
					source = TestChildContextConfig::class,
				)

			val testBeanName = TestChildContextConfig::testBean.name

			val parentContextContainsBean =
				parentContext.containsBean(testBeanName)
			val childContext1ContainsBean =
				childContext1.containsBean(testBeanName)
			val childContext2ContainsBean =
				childContext2.containsBean(testBeanName)

			assertThat(parentContextContainsBean)
				.describedAs("parent context contains $testBeanName")
				.isFalse

			assertThat(childContext1ContainsBean)
				.describedAs("child context 1 contains $testBeanName")
				.isTrue

			assertThat(childContext2ContainsBean)
				.describedAs("child context 2 contains $testBeanName")
				.isTrue

			val childContextId1FromEnvironment =
				childContext1.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

			val childContextId2FromEnvironment =
				childContext2.environment[CHILD_CONTEXT_ID_PROPERTY_NAME]

			assertThat(childContextId1FromEnvironment)
				.describedAs("childContext1Id from environment")
				.isEqualTo(childContextId1)

			assertThat(childContextId2FromEnvironment)
				.describedAs("childContext2Id from environment")
				.isEqualTo(childContextId2)
		}

		@Test
		fun `test create for multiple configuration sources`() {

			val childContextId = "testing"

			val childContext: ApplicationContext =
				childContextService.createIfAbsent(
					childContextId,
					sources = setOf(
						TestChildContextConfig::class,
						TestChildContextConfig2::class,
					),
				)

			val testBeanName1 = TestChildContextConfig::testBean.name
			val testBeanName2 = TestChildContextConfig2::testBean2.name

			val childContextContainsBean1 =
				childContext.containsBean(testBeanName1)
			val childContextContainsBean2 =
				childContext.containsBean(testBeanName2)

			assertThat(childContextContainsBean1)
				.describedAs("child context contains $testBeanName1")
				.isTrue

			assertThat(childContextContainsBean2)
				.describedAs("child context contains $testBeanName2")
				.isTrue
		}

		@Test
		fun `test create for no configuration sources`() {

			val childContextId = "testing"

			assertThatIllegalArgumentException().isThrownBy {
				childContextService.createIfAbsent(
					childContextId,
					sources = emptySet(),
				)
			}.withMessageContaining(NO_SOURCES_MESSAGE)
		}
	}

	@Nested
	inner class TestRead {

		@Test
		fun `test list for empty`() {

			val childContextIds = childContextService.list()

			assertThat(childContextIds).isEmpty()
		}

		@Test
		fun `test list for non-empty`() {

			val childContextId = "testing"

			childContextService.createIfAbsent(
				childContextId,
				source = TestChildContextConfig::class,
			)

			val childContextIds = childContextService.list()

			assertThat(childContextIds).containsOnly(childContextId)
		}

		@Test
		fun `test get for doesn't exist`() {

			val childContextId = "testChildContextId"

			val childContext =
				childContextService.get(childContextId)

			assertThat(childContext)
				.describedAs("childContext")
				.isNull()
		}

		@Test
		fun `test get for does exist`() {

			val childContextId = "testChildContextId"

			childContextService.createIfAbsent(
				childContextId,
				source = TestChildContextConfig::class
			)

			val childContext =
				childContextService.get(childContextId)

			assertThat(childContext)
				.describedAs("childContext")
				.isNotNull
		}
	}

	@Nested
	inner class TestDelete {

		@Test
		fun `test remove and close for doesn't exist`() {

			val childApplicationContext =
				childContextService.removeAndCloseIfPresent("nonExistentId")

			assertThat(childApplicationContext)
				.describedAs("childApplicationContext")
				.isNull()
		}

		@Test
		fun `test remove and close for does exist`() {

			val childContextId = "testChildContextId"

			childContextService.createIfAbsent(
				childContextId,
				source = TestChildContextConfig::class
			)

			val childApplicationContext =
				childContextService.removeAndCloseIfPresent(childContextId)

			assertThat(childApplicationContext)
				.describedAs("childApplicationContext")
				.isNotNull

			assertThat(childApplicationContext?.isRunning)
				.describedAs("isRunning")
				.isFalse
		}
	}

	// Defined private so as not to contribute configuration to the test itself,
	// which is the parent context for the test.

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
}
