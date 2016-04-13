package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;

import com.hammam.business.om.controller.DBTable;

import java.util.List;


/**
 * The persistent class for the porder_status database table.
 * 
 */
@Entity
@Table(name="porder_status")
@NamedQuery(name="PorderStatus.findAll", query="SELECT p FROM PorderStatus p")
public class PorderStatus extends DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	private String description;


	public PorderStatus() {
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
		if(obj != null && obj instanceof PorderStatus){
			PorderStatus ica = (PorderStatus)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}
}