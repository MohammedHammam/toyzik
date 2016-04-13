package com.hammam.business.om.jpa;

import javax.persistence.*;

import com.hammam.business.om.controller.DBTable;

import java.io.Serializable;


/**
 * The persistent class for the currency database table.
 * 
 */
@Entity
@NamedQuery(name="Currency.findAll", query="SELECT c FROM Currency c")
public class Currency extends DBTable implements Serializable {
	/**
	 * @param rowId
	 * @param key
	 * @param toSarRate
	 * @param pitems
	 * @param porders
	 */
	public Currency(String key, float toSarRate) {
		super(key);
		this.key = key;
		this.toSarRate = toSarRate;
	}

	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	@Column(name="TO_SAR_RATE")
	private float toSarRate;

	public Currency() {
		super(null);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public float getToSarRate() {
		return this.toSarRate;
	}

	public void setToSarRate(float toSarRate) {
		this.toSarRate = toSarRate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Currency){
			Currency ica = (Currency)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}
	
}