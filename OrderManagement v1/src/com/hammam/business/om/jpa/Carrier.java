package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the carrier database table.
 * 
 */
@Entity
@NamedQuery(name="Carrier.findAll", query="SELECT c FROM Carrier c")
public class Carrier extends com.hammam.business.om.controller.DBTable implements Serializable {
	/**
	 * @param rowId
	 * @param key
	 * @param ptrackings
	 */
	public Carrier(String key) {
		super(key);
		this.key = key;
	}

	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	public Carrier() {
		super(null);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Carrier){
			Carrier ica = (Carrier)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}

}