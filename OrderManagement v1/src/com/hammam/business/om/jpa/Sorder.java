package com.hammam.business.om.jpa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.hammam.business.om.dao.LOVS;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sorder database table.
 * 
 */
@Entity
@NamedQuery(name="Sorder.findAll", query="SELECT s FROM Sorder s")
public class Sorder extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int seqId;

	private String comment;

	private String contact1;

	private String contact2;

	private Date creation_Date;

	@Column(name="customer_name")
	private String customerName;

	@Column(name="delivery_cost")
	private float deliveryCost;
	
	@Transient
	private float totalValue =0.0f;
	
	@Transient 
	private int noItems;
	
	@Transient 
	private float totalDiscount;

	private String location;

	//bi-directional many-to-one association to Sitem
	@OneToMany(mappedBy="sorder")
	private List<Sitem> sitems;

	//bi-directional many-to-one association to DeliveryType
	@ManyToOne
	@JoinColumn(name="delivery_type")
	private DeliveryType deliveryTypeBean;

	//bi-directional many-to-one association to SorderStatus
	@ManyToOne
	@JoinColumn(name="status")
	private SorderStatus sorderStatus;

	public Sorder() {
		super(null);
		comment ="";
		creation_Date = new Date(System.currentTimeMillis());
		customerName = "";
		deliveryCost = 0.0f;
		sorderStatus = LOVS.SORDER_STATUS_MAP.get("Under Collection");
		deliveryTypeBean = LOVS.DELIVERY_TYPE_MAP.get("Pickup");
		contact1 ="";
		contact2 ="";
	}

	public int getSeqId() {
		return this.seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContact1() {
		return this.contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return this.contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public Date getCreation_Date() {
		return this.creation_Date;
	}

	public void setCreation_Date(Date creation_Date) {
		this.creation_Date = creation_Date;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public float getDeliveryCost() {
		return this.deliveryCost;
	}

	public void setDeliveryCost(float deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Sitem> getSitems() {
		return this.sitems;
	}

	public void setSitems(List<Sitem> sitems) {
		this.sitems = sitems;
	}

	public Sitem addSitem(Sitem sitem) {
		getSitems().add(sitem);
		sitem.setSorder(this);

		return sitem;
	}

	public Sitem removeSitem(Sitem sitem) {
		getSitems().remove(sitem);
		sitem.setSorder(null);

		return sitem;
	}

	public DeliveryType getDeliveryTypeBean() {
		return this.deliveryTypeBean;
	}

	public void setDeliveryTypeBean(DeliveryType deliveryTypeBean) {
		this.deliveryTypeBean = deliveryTypeBean;
	}

	public SorderStatus getSorderStatus() {
		return this.sorderStatus;
	}

	public void setSorderStatus(SorderStatus sorderStatus) {
		this.sorderStatus = sorderStatus;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Sorder){
			Sorder ica = (Sorder)obj;
			if(this.seqId == ica.getSeqId())
				return true;
		}
		return false;
	}
	
		public String getFormatedSeqId(){
			return "SO-"+String.format("%05d", seqId);
		}

		/**
		 * @return the totalValue
		 */
		public float getTotalValue() {
			return totalValue;
		}

		/**
		 * @param totalValue the totalValue to set
		 */
		public void setTotalValue(float totalValue) {
			this.totalValue = totalValue;
		}

		/**
		 * @return the noItems
		 */
		public int getNoItems() {
			return noItems;
		}

		/**
		 * @param noItems the noItems to set
		 */
		public void setNoItems(int noItems) {
			this.noItems = noItems;
		}

		/**
		 * @return the totalDiscount
		 */
		public float getTotalDiscount() {
			return totalDiscount;
		}

		/**
		 * @param totalDiscount the totalDiscount to set
		 */
		public void setTotalDiscount(float totalDiscount) {
			this.totalDiscount = totalDiscount;
		}
		
		
}