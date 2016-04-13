/**
 * 
 */
package com.hammam.business.om.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hammam.business.om.dao.LOVS;
import com.hammam.business.om.dao.LOVsDAO;
import com.hammam.business.om.jpa.Currency;
import com.hammam.business.om.jpa.DeliveryType;
import com.hammam.business.om.jpa.ItemType;
import com.hammam.business.om.jpa.PitemStatus;
import com.hammam.business.om.jpa.PorderStatus;
import com.hammam.business.om.jpa.Site;
import com.hammam.business.om.jpa.SorderStatus;

/**
 * @author Hammam
 *
 */
@ManagedBean(name = "lovsCtrl", eager = true) 
@Service(value = "lovsCtrl")
public class LOVsHandler implements Serializable {
	
	@Autowired
	LOVsDAO lovsDAO;
	
	private List<String> sitesList;
	private List<String> porderStatusList;
	private List<String> pitemStatusList;
	private List<String> currencyList;
	private List<String> carriersList;
	private List<String> ptrackingList;
	private List<String> deliveryTypeList;
	private List<String> sorderStatusList;
	private List<String> itemTypesList;
	/**
	 * @return the sitesList
	 */
	public List<String> getSitesList() {
		if(sitesList == null)
			sitesList = new ArrayList<String>(LOVS.SITE_MAP.keySet());
		return sitesList;
	}
	/**
	 * @return the porderStatusList
	 */
	public List<String> getPorderStatusList() {
		if(porderStatusList == null)
			porderStatusList = new ArrayList<String>(LOVS.PORDER_STATUS_MAP.keySet());
		return porderStatusList;
	}
	/**
	 * @return the pitemStatusList
	 */
	public List<String> getPitemStatusList() {
		if(pitemStatusList == null)
			pitemStatusList = new ArrayList<String>(LOVS.PITEM_STATUS_MAP.keySet());
		return pitemStatusList;
	}
	/**
	 * @return the currencyList
	 */
	public List<String> getCurrencyList() {
		if(currencyList == null)
			currencyList = new ArrayList<String>(LOVS.CURRENCY_MAP.keySet());
		return currencyList;
	}
	/**
	 * @return the carriersList
	 */
	public List<String> getCarriersList() {
		if(carriersList == null)
			carriersList = new ArrayList<String>(LOVS.CARRIER_MAP.keySet());
		return carriersList;
	}
	/**
	 * @return the ptrackingList
	 */
	public List<String> getPtrackingList() {
		if(ptrackingList == null)
			ptrackingList = new ArrayList<String>(LOVS.PTRACK_STATUS_MAP.keySet());
		return ptrackingList;
	}
	
	/**
	 * @return the itemTypeList
	 */
	public List<String> getItemTypeList() {
		if(itemTypesList == null)
			itemTypesList = new ArrayList<String>(LOVS.ITEM_TYPE_MAP.keySet());
		return itemTypesList;
	}
	
	
	/**
	 * @return the deliveryTypeList
	 */
	public List<String> getDeliveryTypeList() {
		if(deliveryTypeList == null)
			deliveryTypeList = new ArrayList<String>(LOVS.DELIVERY_TYPE_MAP.keySet());
		return deliveryTypeList;
	}
	/**
	 * @return the sorderStatusList
	 */
	public List<String> getSorderStatusList() {
		if(sorderStatusList == null)
			sorderStatusList = new ArrayList<String>(LOVS.SORDER_STATUS_MAP.keySet());
		return sorderStatusList;
	}
	/**
	 * @return the lovsDAO
	 */
	public LOVsDAO getLovsDAO() {
		return lovsDAO;
	}
	/**
	 * @param lovsDAO the lovsDAO to set
	 */
	public void setLovsDAO(LOVsDAO lovsDAO) {
		this.lovsDAO = lovsDAO;
	}
	
	
	public HashMap<String, PorderStatus> getPOrderStatusMap(){
		return LOVS.PORDER_STATUS_MAP;
	}
	
	public HashMap<String, PitemStatus> getPItemStatusMap(){
		return LOVS.PITEM_STATUS_MAP;
	}
	
	public HashMap<String, Site> getSiteMap(){
		return LOVS.SITE_MAP;
	}
	
	public HashMap<String, Currency> getCurrencyMap(){
		return LOVS.CURRENCY_MAP;
	}
	
	public HashMap<String, DeliveryType> getDeliveryTypeMap(){
		return LOVS.DELIVERY_TYPE_MAP;
	}
	
	public HashMap<String, ItemType> getItemTypeMap(){
		return LOVS.ITEM_TYPE_MAP;
	}
	
	public HashMap<String, SorderStatus> getSorderStatusMap(){
		return LOVS.SORDER_STATUS_MAP;
	}
	
	public ArrayList<Currency> getCurrencyObjectsList(){
		ArrayList<Currency> valuesList = new ArrayList<Currency>(LOVS.CURRENCY_MAP.values());
		return valuesList;
	}

}
