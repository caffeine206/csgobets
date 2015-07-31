package com.csgobets.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class MvcConfiguringPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof HttpMessageConverter<?>) { 
			 if (bean instanceof MappingJackson2HttpMessageConverter) {  
			   ((MappingJackson2HttpMessageConverter) bean).setPrettyPrint(true);     
			  }  
		} 
	    return bean;  
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
