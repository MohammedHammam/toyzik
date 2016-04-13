package com.hammam.business.om.test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hammam.business.om.dao.LOVS;
import com.hammam.business.om.dao.LOVsDAO;
import com.hammam.business.om.dao.POrederDAO;
import com.hammam.business.om.jpa.Porder;

public class App 
{
    public static void main( String[] args ) throws SQLException
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/Spring-Module.xml");
    	 
    	LOVsDAO lovsDAO = (LOVsDAO) context.getBean("lovsDAO");
        POrederDAO orderDAO = (POrederDAO) context.getBean("orderDAO");
        Porder porder = new Porder();
        porder.setSite(LOVS.SITE_MAP.get("SAR"));
        porder.setComment("Commenttt");
        porder.setOrderId("34634364");
        porder.setValue(new BigDecimal("100"));
        porder.setCreationdate(new Timestamp(System.currentTimeMillis()));
        porder.setCurrencyBean(LOVS.CURRENCY_MAP.get("SAR"));
        porder.setPorderStatus(LOVS.PORDER_STATUS_MAP.get("Paied"));
        
        orderDAO.insertOrder(porder);
    	
        Porder porder1 = orderDAO.getOrderById("34634364");
        System.out.println(porder1.toString());
        
    }
}
