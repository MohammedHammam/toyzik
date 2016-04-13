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

/**
 * @author Hammam
 *
 */
public interface PItemDAO {
	public byte[] getItemImageById(String id);
	public String insertItem(Pitem pitem) throws SQLException;
	public String updateItem(Pitem pitem) throws Exception;
	public List<Pitem> listItems();
	public Pitem getItemById(String Id);
	public List<Pitem> getItemByName(String name);
	public List<Pitem> listItemsOfOrder(String orderId);
	public List<Pitem> searchItems(String[] site, String orderId, String[] status, String [] s_itemTypes, int valueFrom, int valueTo, int svalueFrom, int svalueTo, String name,
			Date createdFrom, Date createdTo, Date orderedFrom, Date orderedTo, int quantityFrom, int quantityTo, int stockFrom, int stockTo);
	public List<Pitem> listItemsOfExactOrder(String orderId);
	public List<Pitem> listAvailableItems();
	List<Pitem> listItemsByType(String[] itemTypes);
	List<Pitem> listAvailableItemsByType(String[] itemTypes);


}
