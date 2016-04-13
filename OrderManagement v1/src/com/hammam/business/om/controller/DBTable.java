package com.hammam.business.om.controller;

import java.io.Serializable;
import java.util.HashMap;


/**
 * @author m.hammam.ejd
 *
 */
public class DBTable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3945866695954084001L;

	private String rowId; 
	
	private HashMap<String, String> extraInfo;

	
	
	/**
	 * @return the extraInfo
	 */
	public HashMap<String, String> getExtraInfo() {
		return extraInfo;
	}

	/**
	 * @param extraInfo the extraInfo to set
	 */
	public void setExtraInfo(HashMap<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	/**
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	/**
	 * @param rowId
	 */
	public DBTable(String rowId) {
		super();
		this.rowId = rowId;
		extraInfo = new HashMap<String, String>();
	}
	
	public void addExtraInfo(String k, String v){
		extraInfo.put(k, v);
	}
	
	public String getExtraInfoValue(String k){
		return extraInfo.get(k);
	}
	

}
