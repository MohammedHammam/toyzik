package com.hammam.business.om.jpa;

import java.io.Serializable;
import javax.persistence.*;

import com.hammam.business.om.controller.DBTable;
import com.hammam.business.om.controller.LOVsHandler;
import com.hammam.business.om.dao.LOVS;
import com.hammam.business.om.dao.LOVsDAO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * The persistent class for the porder database table.
 * 
 */
@Entity
@NamedQuery(name="Porder.findAll", query="SELECT p FROM Porder p")
public class Porder extends DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQ_ID")
	private int seqId; 

	@Column(name="`COMMENT`")
	private String comment;

	private Date creationdate;

	@Column(name="ORDER_ID")
	private String orderId;

	private Date orderdate;

	private Site site;

	private BigDecimal value;

	//bi-directional one-to-one association to Pitem
	@OneToMany(mappedBy="porder")
	private List<Pitem> pitems;

	//bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name="CURRENCY")
	private Currency currencyBean;

	//bi-directional many-to-one association to PorderStatus
	@ManyToOne
	@JoinColumn(name="STATUS")
	private PorderStatus porderStatus;

	//bi-directional many-to-one association to Ptracking
	@OneToMany(mappedBy="porder")
	private List<Ptracking> ptrackings;
	
	@Transient
	private HashMap<String, byte[]> itemsPhotosMap = new HashMap<String, byte[]>();

	private float EFC;
	
	
	public Porder() {
		super(null);
		comment ="";
		orderdate = new Date(System.currentTimeMillis());
		creationdate = new Date(System.currentTimeMillis());
		value = new BigDecimal(0);
		site = LOVS.SITE_MAP.get("Local Market");
		porderStatus = LOVS.PORDER_STATUS_MAP.get("Paid");
		currencyBean = LOVS.CURRENCY_MAP.get("SAR");
		EFC = 1.0f;
		orderId ="";
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

	public Date getCreationdate() {
		return this.creationdate;
	}

	/**
	 * @param seqId
	 * @param comment
	 * @param creationdate
	 * @param orderId
	 * @param orderdate
	 * @param site
	 * @param value
	 * @param currencyBean
	 * @param porderStatus
	 */
	public Porder(int seqId, String comment, Date creationdate, String orderId, Date orderdate,
			Site site, BigDecimal value, Currency currencyBean, PorderStatus porderStatus, float EFC) {
		super(seqId+"");
		this.seqId = seqId;
		this.comment = comment;
		this.creationdate = creationdate;
		this.orderId = orderId;
		this.orderdate = orderdate;
		this.site = site;
		this.value = value;
		this.currencyBean = currencyBean;
		this.porderStatus = porderStatus;
		this.EFC = EFC;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getOrderdate() {
		return this.orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = new BigDecimal(value);
	}

	public List<Pitem> getPitems() {
		return this.pitems;
	}

	public void setPitems(List<Pitem> pitems) {
		this.pitems = pitems;
		if(pitems != null)
		for(Pitem pitem: pitems){
			getItemsPhotosMap().put(pitem.getSeqId()+"", pitem.getImageBytes());
		}
	}
	
	public Pitem addPitem(Pitem pitem) {
		getPitems().add(pitem);
		pitem.setPorder(this);

		return pitem;
	}

	public Currency getCurrencyBean() {
		return this.currencyBean;
	}

	public void setCurrencyBean(Currency currencyBean) {
		this.currencyBean = currencyBean;
	}

	public PorderStatus getPorderStatus() {
		return this.porderStatus;
	}

	public void setPorderStatus(PorderStatus porderStatus) {
		this.porderStatus = porderStatus;
	}

	public List<Ptracking> getPtrackings() {
		return this.ptrackings;
	}

	public void setPtrackings(List<Ptracking> ptrackings) {
		this.ptrackings = ptrackings;
	}

	public Ptracking addPtracking(Ptracking ptracking) {
		getPtrackings().add(ptracking);
		ptracking.setPorder(this);

		return ptracking;
	}

	public Ptracking removePtracking(Ptracking ptracking) {
		getPtrackings().remove(ptracking);
		ptracking.setPorder(null);

		return ptracking;
	}
	public String getFormatedSeqId(){
		return "PO-"+String.format("%05d", seqId);
	}

	/**
	 * @return the itemsPhotosMap
	 */
	public HashMap<String, byte[]> getItemsPhotosMap() {
		return itemsPhotosMap;
	}

	/**
	 * @return the eFC
	 */
	public float getEFC() {
		return EFC;
	}

	/**
	 * @param eFC the eFC to set
	 */
	public void setEFC(float eFC) {
		EFC = eFC;
	}

	
}