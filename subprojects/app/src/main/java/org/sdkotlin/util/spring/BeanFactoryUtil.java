package org.sdkotlin.util.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

/**
 * Utility for
 * <a href="https://github.com/spring-projects/spring-framework/issues/31444">spring-projects/spring-framework#31444</a>.
 */
public class BeanFactoryUtil {

	private final BeanFactory beanFactory;

	public BeanFactoryUtil(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public <T> T getBean(ParameterizedTypeReference<T> parameterizedTypeReference) {

		return beanFactory.<T>getBeanProvider(
			ResolvableType.forType(parameterizedTypeReference)
		).getObject();
	}
}
