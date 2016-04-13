package com.hammam.business.om.controller;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


/**
 * @author m.hammam.ejd
 *
 */
public class DBTableDataModel<T> extends ListDataModel<T> implements SelectableDataModel<T>{
	
	private T t;
	
	

	/**
	 * @return the t
	 */
	public T getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(T t) {
		this.t = t;
	}

	public DBTableDataModel(List<T> data) {  
        super(data);  
    }  
	@Override
	public Object getRowKey(T pin) {
		return ((DBTable) pin).getRowId();
	}
	@Override
	public T getRowData(String rowKey) {
		List<T> pins = (List<T>) getWrappedData();  
        if(pins!=null)
        for(T pin : pins) {  
            if(((DBTable) pin).getRowId().equals(rowKey))  
                return pin;  
        }  
        return null;
	}

	
}
