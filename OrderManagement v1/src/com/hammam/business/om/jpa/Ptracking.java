package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ptracking database table.
 * 
 */
@Entity
@NamedQuery(name="Ptracking.findAll", query="SELECT p FROM Ptracking p")
public class Ptracking extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQ_ID")
	private int seqId;

	@Column(name="`COMMENT`")
	private String comment;

	private Timestamp creationdate;

	private Timestamp orderdate;

	private String site;

	@Column(name="SITE_ORDER_ID")
	private String siteOrderId;

	private String tnumber;

	//bi-directional many-to-one association to Carrier
	@ManyToOne
	@JoinColumn(name="TYPE")
	private Carrier carrier;

	//bi-directional many-to-one association to Porder
	@ManyToOne
	@JoinColumn(name="PORDER_ID")
	private Porder porder;

	//bi-directional many-to-one association to PtrackingStatus
	@ManyToOne
	@JoinColumn(name="STATUS")
	private PtrackingStatus ptrackingStatus;

	public Ptracking() {
		super(null);
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

	public Timestamp getCreationdate() {
		return this.creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public Timestamp getOrderdate() {
		return this.orderdate;
	}

	public void setOrderdate(Timestamp orderdate) {
		this.orderdate = orderdate;
	}

	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSiteOrderId() {
		return this.siteOrderId;
	}

	public void setSiteOrderId(String siteOrderId) {
		this.siteOrderId = siteOrderId;
	}

	public String getTnumber() {
		return this.tnumber;
	}

	public void setTnumber(String tnumber) {
		this.tnumber = tnumber;
	}

	public Carrier getCarrier() {
		return this.carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public Porder getPorder() {
		return this.porder;
	}

	public void setPorder(Porder porder) {
		this.porder = porder;
	}

	public PtrackingStatus getPtrackingStatus() {
		return this.ptrackingStatus;
	}

	public void setPtrackingStatus(PtrackingStatus ptrackingStatus) {
		this.ptrackingStatus = ptrackingStatus;
	}

}