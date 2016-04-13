/**
 * 
 */
package com.hammam.business.om.dao;

import java.util.List;

import com.hammam.business.om.jpa.Pitem;
import com.hammam.business.om.jpa.Porder;

/**
 * @author Hammam
 *
 */
public interface PTrackingDAO {
	
	public String saveItem(Pitem pitem);
	public List<Pitem> listItems();
	public Pitem getItemById(String Id);

}
