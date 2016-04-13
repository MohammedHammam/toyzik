/**
 * 
 */
package com.hammam.business.om.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Hammam
 *
 */
@Component(value = "handler")
public class AbstractHandler implements ApplicationContextAware{
	protected static ApplicationContext context; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		
	}

	
	
	
}
