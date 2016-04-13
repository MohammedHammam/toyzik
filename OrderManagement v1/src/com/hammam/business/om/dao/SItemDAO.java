/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.hammam.business.om.jpa.Pitem;
import com.hammam.business.om.jpa.Porder;
import com.hammam.business.om.jpa.Sitem;

/**
 * @author Hammam
 *
 */
public interface SItemDAO {
	public byte[] getItemImageById(String id);
	public String insertItem(Sitem sitem) throws SQLException;
	public String updateItem(Sitem sitem) throws Exception;
	public List<Sitem> listItems();
//	public Sitem getItemById(String Id);
//	public List<Sitem> getItemByName(String name);
	public List<Sitem> listItemsOfOrder(int orderId);
//	public List<Sitem> searchItems(String[] site, String orderId, String[] status, int valueFrom, int valueTo, int svalueFrom, int svalueTo, String name,
//			Date createdFrom, Date createdTo, Date orderedFrom, Date orderedTo, int quantityFrom, int quantityTo, int stockFrom, int stockTo);
//	public List<Sitem> listItemsOfExactOrder(String orderId);


}
