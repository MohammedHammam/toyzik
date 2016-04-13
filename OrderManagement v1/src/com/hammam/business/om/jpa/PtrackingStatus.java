package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ptracking_status database table.
 * 
 */
@Entity
@Table(name="ptracking_status")
@NamedQuery(name="PtrackingStatus.findAll", query="SELECT p FROM PtrackingStatus p")
public class PtrackingStatus extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	private String description;


	public PtrackingStatus() {
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
		if(obj != null && obj instanceof PtrackingStatus){
			PtrackingStatus ica = (PtrackingStatus)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}

}