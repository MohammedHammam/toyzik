/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.hammam.business.om.jpa.Porder;

/**
 * @author Hammam
 *
 */
public interface POrederDAO {
	
	public String insertOrder(Porder porder) throws SQLException;
	public String updateOrder(Porder porder) throws Exception;
	public List<Porder> listOrderes();
	public Porder getOrderById(String Id);
	public List<Porder> searchOrderes(String[] site, String orderId, String[] status, int valueFrom, int valueTo,
			Date createdFrom, Date createdTo, Date orderedFrom, Date orderedTo);

}
