package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the pitem_status database table.
 * 
 */
@Entity
@Table(name="pitem_status")
@NamedQuery(name="PitemStatus.findAll", query="SELECT p FROM PitemStatus p")
public class PitemStatus extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	private String description;


	public PitemStatus() {
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
		if(obj != null && obj instanceof PitemStatus){
			PitemStatus ica = (PitemStatus)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}
}