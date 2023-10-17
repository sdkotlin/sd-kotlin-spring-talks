package org.sdkotlin.util.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for
 * <a href="https://github.com/spring-projects/spring-framework/issues/31444">spring-projects/spring-framework#31444</a>.
 */
@SpringBootTest
class SpringJavaGetBeanNestedGenericIT {

	private static final List<String> LIST_OF_STRING =
		new ArrayList<>();

	private static final List<List<String>> LIST_OF_LIST_OF_STRING =
		new ArrayList<>();

	static {
		LIST_OF_STRING.add("Testing");
		var nestedListOfString = new ArrayList<String>();
		nestedListOfString.add("Testing");
		LIST_OF_LIST_OF_STRING.add(nestedListOfString);
	}

	private final BeanFactoryUtil beanFactoryUtil;

	@Configuration
	static class TestConfig {

		@Bean
		public List<String> getListOfString() {
			return LIST_OF_STRING;
		}

		@Bean
		public List<List<String>> getListOfListOfString() {
			return LIST_OF_LIST_OF_STRING;
		}
	}

	public SpringJavaGetBeanNestedGenericIT(
		@Autowired
		ApplicationContext applicationContext
	) {
		beanFactoryUtil = new BeanFactoryUtil(applicationContext);
	}

	@Test
	void testListOfStringIsInjected(
		@Autowired
		List<String> listOfString
	) {
		assertThat(listOfString).isEqualTo(LIST_OF_STRING);
	}

	@Test
	void testListOfListOfStringIsInjected(
		@Autowired
		List<List<String>> listOfListOfString
	) {
		assertThat(listOfListOfString).isEqualTo(LIST_OF_LIST_OF_STRING);
	}

	@Test
	void testGenericGetBeanForListOfString() {

		var listOfString =
			beanFactoryUtil.getBean(
				new ParameterizedTypeReference<List<String>>() {
				}
			);

		assertThat(listOfString).isEqualTo(LIST_OF_STRING);
	}

	@Test
	void testGenericGetBeanForListOfListOfString() {

		var listOfListOfString =
			beanFactoryUtil.getBean(
				new ParameterizedTypeReference<List<List<String>>>() {
				}
			);

		assertThat(listOfListOfString).isEqualTo(LIST_OF_LIST_OF_STRING);
	}

	@Test
	void testGetBeanProviderForListOfListOfString(
		@Autowired
		ApplicationContext applicationContext
	) {
		final ParameterizedTypeReference<List<List<String>>> typeReference =
			new ParameterizedTypeReference<>() {
			};

		final ResolvableType resolvableType =
			ResolvableType.forType(typeReference);

		final ObjectProvider<List<List<String>>> objectProvider =
			applicationContext.getBeanProvider(resolvableType);

		final List<List<String>> listOfListOfString =
			objectProvider.getObject();

		assertThat(listOfListOfString).isEqualTo(LIST_OF_LIST_OF_STRING);
	}
}
