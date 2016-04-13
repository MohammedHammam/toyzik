package com.hammam.business.om.jpa;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.persistence.*;

import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.hammam.business.om.dao.LOVS;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;


/**
 * The persistent class for the pitem database table.
 * 
 */
@Entity
@NamedQuery(name="Pitem.findAll", query="SELECT p FROM Pitem p")
public class Pitem extends com.hammam.business.om.controller.DBTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQ_ID")
	private int seqId;
	
	@Column(name="NAME")
	private String name;

	@Column(name="`COMMENT`")
	private String comment;

	private Date creationdate;

	@Column(name="DELIVERED_QUANTITY")
	private int deliveredQuantity;

	@Column(name="MAX_DISCOUNT")
	private BigDecimal maxDiscount;
	
	@Column(name="PHOTO1")
	private byte[] imageBytes;

	private Date orderdate;

	private int quantity;

	@Column(name="QUANTITY_IN_STOCK")
	private int quantityInStock;

	@Column(name="SELLING_VALUE")
	private BigDecimal sellingValue;

	private String site;

	@Column(name="SITE_ORDER_ID")
	private String siteOrderId;

	private String url;

	private BigDecimal value;
	
	private ItemType type;

	//bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name="CURRENCY")
	private Currency currencyBean;

	//bi-directional many-to-one association to PitemStatus
	@ManyToOne
	@JoinColumn(name="STATUS")
	private PitemStatus pitemStatus;

	@Transient
	private StreamedContent steamedImage;
	

	//bi-directional many-to-one association to Porder
	@ManyToOne
	@JoinColumn(name="PORDER_ID")
	private Porder porder;

	public Pitem() {
		super(null);
		comment ="";
		orderdate = new Date(System.currentTimeMillis());
		creationdate = new Date(System.currentTimeMillis());
		value = new BigDecimal(0);
		sellingValue = new BigDecimal(0);
		maxDiscount = new BigDecimal(0);
		quantity = 0;
		deliveredQuantity = 0;
		quantityInStock = 0;
		url = "";	
		site = "";
		name = "";
		currencyBean = LOVS.CURRENCY_MAP.get("SAR");
		siteOrderId ="";
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

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public int getDeliveredQuantity() {
		return this.deliveredQuantity;
	}

	public void setDeliveredQuantity(int deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}

	public BigDecimal getMaxDiscount() {
		return this.maxDiscount;
	}

	public void setMaxDiscount(BigDecimal maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public Date getOrderdate() {
		return this.orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantityInStock() {
		return this.quantityInStock;
	}

	public void setQuantityInStock(int quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public BigDecimal getSellingValue() {
		return this.sellingValue;
	}

	public void setSellingValue(BigDecimal sellingValue) {
		this.sellingValue = sellingValue;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Currency getCurrencyBean() {
		return this.currencyBean;
	}

	public void setCurrencyBean(Currency currencyBean) {
		this.currencyBean = currencyBean;
	}

	public PitemStatus getPitemStatus() {
		return this.pitemStatus;
	}

	public void setPitemStatus(PitemStatus pitemStatus) {
		this.pitemStatus = pitemStatus;
	}

	public Porder getPorder() {
		return this.porder;
	}

	public void setPorder(Porder porder) {
		this.porder = porder;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return the imageBytes
	 */
	public byte[] getImageBytes() {
		return imageBytes;
	}

	/**
	 * @param imageBytes the imageBytes to set
	 */
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
		steamedImage = new DefaultStreamedContent(new ByteArrayInputStream(imageBytes==null?new byte[0]:imageBytes), "image/jpg");
		
	}

	public String getFormatedSeqId(){
		return "PI-"+String.format("%05d", seqId);
	}
	
	
	public StreamedContent getSteamedImage(){
		steamedImage = new DefaultStreamedContent(new ByteArrayInputStream(imageBytes==null?new byte[0]:imageBytes), "image/jpg");
		
		 return steamedImage;
//		FacesContext context = FacesContext.getCurrentInstance();
//
//        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
//            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
//            return new DefaultStreamedContent();
//        }
//        else {
//            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
//            return new DefaultStreamedContent(new ByteArrayInputStream(imageBytes));
//        }
//		return new ByteArrayContent(imageBytes, "image/jpg");
	} 
	
	public float getValueInSAR(){
		return (value.floatValue() * currencyBean.getToSarRate() * porder.getEFC());
	}

	/**
	 * @param steamedImage the steamedImage to set
	 */
	public void setSteamedImage(StreamedContent steamedImage) {
		this.steamedImage = steamedImage;
	}

	/**
	 * @return the type
	 */
	public ItemType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ItemType type) {
		this.type = type;
	}

	
	
}