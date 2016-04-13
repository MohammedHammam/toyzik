package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the site database table.
 * 
 */
@Entity
@NamedQuery(name="Site.findAll", query="SELECT s FROM Site s")
public class Site extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="`KEY`")
	private String key;

	private String name;

	public Site() {
		super(null);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Site){
			Site ica = (Site)obj;
			if(this.key.equals(ica.getKey()))
				return true;
		}
		return false;
	}
}