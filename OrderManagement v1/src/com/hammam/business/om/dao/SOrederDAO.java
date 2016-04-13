/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.hammam.business.om.jpa.Porder;
import com.hammam.business.om.jpa.Sorder;

/**
 * @author Hammam
 *
 */
public interface SOrederDAO {
	
	public String insertOrder(Sorder sorder) throws SQLException;
	public String updateOrder(Sorder sorder) throws Exception;
	public List<Sorder> listOrderes();
	public List<Sorder> getOrderByCustomerId(String customerName);
	public List<Sorder> getActiveOrderByCustomerId(String customerName);
	public List<Sorder> searchOrderes(String customerName, String contact, String[] status, int valueFrom, int valueTo,
			Date createdFrom, Date createdTo, String[] deliveryType);
	public Sorder getOrderBySeqId(String Id);
	public Sorder getOrderByExactCustomerName(String customerName);

}
