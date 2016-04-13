package com.hammam.business.om.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author m.hammam.ejd
 *
 */
public abstract class DataSourceDAO {

	
	protected DataSource dataSource;
	
	
	
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	
	/**
	 * 
	 */
	public DataSourceDAO() {
		
		//new ClassPathXmlApplicationContext();
		AbstractApplicationContext ac = new ClassPathXmlApplicationContext("/META-INF/Spring-Module.xml");
		dataSource = (DataSource) ac.getBean("dataSource");
	}

	
	
}
