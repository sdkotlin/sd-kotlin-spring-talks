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

@SpringBootTest
internal class ChildContextServiceIT {

	@Configuration
	internal class TestConfig {

		@Bean
		fun childContextService(
			applicationContext: ConfigurableApplicationContext
		) = ChildContextService(applicationContext)
	}

	@Test
	fun `test create`(
		@Autowired
		parentContext: ConfigurableApplicationContext,
		@Autowired
		childContextService: ChildContextService
	) {

		val testId = "testing"

		val childContext: ApplicationContext =
			childContextService.create(testId, TestChildContextConfig::class)

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
}

// Defined private and outside the test class so as not to contribute
// configuration to the test itself, which is the parent context for the test.
@Configuration
private class TestChildContextConfig {

	@Bean
	fun testBean() = Unit
}
