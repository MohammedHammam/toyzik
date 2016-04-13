package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the delivery_type database table.
 * 
 */
@Entity
@Table(name="delivery_type")
@NamedQuery(name="DeliveryType.findAll", query="SELECT d FROM DeliveryType d")
public class DeliveryType extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	private String description;

	public DeliveryType() {
		super(null);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof DeliveryType){
			DeliveryType ica = (DeliveryType)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}

}