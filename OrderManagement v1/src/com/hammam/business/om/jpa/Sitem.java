package com.hammam.business.om.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.hammam.business.om.dao.LOVS;


/**
 * The persistent class for the sitem database table.
 * 
 */
@Entity
@NamedQuery(name="Sitem.findAll", query="SELECT s FROM Sitem s")
public class Sitem extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int seqId;

	private float discount;

	private int pitem_seqId;

	private Pitem pitem;
	
	private int quantity;
	
	private String comment;

	//bi-directional many-to-one association to Sorder
	@ManyToOne
	private Sorder sorder;
	
	private boolean isEdit = false;

	public Sitem() {
		super(null);
		pitem = null;
		quantity = 0;
		discount = 0.0f;
	}

	public int getSeqId() {
		return this.seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public float getDiscount() {
		return this.discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public int getPitem_seqId() {
		return this.pitem_seqId;
	}

	public void setPitem_seqId(int pitem_seqId) {
		this.pitem_seqId = pitem_seqId;
	}
 
	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Sorder getSorder() {
		return this.sorder;
	}

	public void setSorder(Sorder sorder) {
		this.sorder = sorder;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Sitem){
			Sitem ica = (Sitem)obj;
			if(this.seqId == ica.getSeqId())
				return true;
		}
		return false;
	}
	
	public String getFormatedSeqId(){
		return "SI-"+String.format("%05d", seqId);
	}
	
	public float getTotalValue(){
		if(pitem != null){
			return quantity * pitem.getSellingValue().floatValue() - discount;
		}
		return 0.0f;
	}
	public float getTotalValueWD(){
		if(pitem != null){
			return quantity * pitem.getSellingValue().floatValue();
		}
		return 0.0f;
	}

	/**
	 * @return the pitem
	 */
	public Pitem getPitem() {
		return pitem;
	}

	/**
	 * @param pitem the pitem to set
	 */
	public void setPitem(Pitem pitem) {
		this.pitem = pitem;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the isEdit
	 */
	public boolean isEdit() {
		return isEdit;
	}

	/**
	 * @param isEdit the isEdit to set
	 */
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
}