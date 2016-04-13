/**
 * 
 */
package com.hammam.business.om.dao;

import java.util.HashMap;

import com.hammam.business.om.jpa.Carrier;
import com.hammam.business.om.jpa.Currency;
import com.hammam.business.om.jpa.DeliveryType;
import com.hammam.business.om.jpa.ItemType;
import com.hammam.business.om.jpa.PitemStatus;
import com.hammam.business.om.jpa.PorderStatus;
import com.hammam.business.om.jpa.PtrackingStatus;
import com.hammam.business.om.jpa.Site;
import com.hammam.business.om.jpa.SorderStatus;

/**
 * @author Hammam
 *
 */
public class LOVS {
	
	public static  HashMap<String, Currency> CURRENCY_MAP = new HashMap<String, Currency>();
	public static  HashMap<String, Carrier> CARRIER_MAP = new HashMap<String, Carrier>();
	public static  HashMap<String, PorderStatus> PORDER_STATUS_MAP = new HashMap<String, PorderStatus>();
	public static  HashMap<String, PtrackingStatus> PTRACK_STATUS_MAP = new HashMap<String, PtrackingStatus>();
	public static  HashMap<String, PitemStatus> PITEM_STATUS_MAP = new HashMap<String, PitemStatus>();
	public static  HashMap<String, Site> SITE_MAP = new HashMap<String, Site>();
	public static  HashMap<String, SorderStatus> SORDER_STATUS_MAP = new HashMap<String, SorderStatus>();
	public static  HashMap<String, DeliveryType> DELIVERY_TYPE_MAP = new HashMap<String, DeliveryType>();
	public static  HashMap<String, ItemType> ITEM_TYPE_MAP = new HashMap<String, ItemType>();
	
}
