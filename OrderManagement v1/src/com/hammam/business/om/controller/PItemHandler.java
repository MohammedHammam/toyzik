/**
 * 
 */
package com.hammam.business.om.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hammam.business.om.dao.PItemDAOImp;
import com.hammam.business.om.dao.POrderDAOImp;
import com.hammam.business.om.dao.PTrackingDAOImp;
import com.hammam.business.om.jpa.Currency;
import com.hammam.business.om.jpa.Pitem;
import com.hammam.business.om.jpa.Porder;
import com.hammam.business.om.jpa.Ptracking;


/**
 * @author Hammam
 *
 */
@ManagedBean(name = "PItemCtrl", eager = true)
@SessionScoped
@Component(value = "PItemCtrl")
public class PItemHandler extends AbstractHandler implements Serializable {

	private static final long serialVersionUID = 1L;
	private POrderDAOImp pOrderDAOImp;
	private PItemDAOImp pItemDAOImp;
	private DBTableDataModel<Pitem> pitems;
	private ArrayList<Pitem> pitemsList;
	private boolean tableView = true;

	private Porder porder;
	private Pitem pitem;
	private int idisplayMode;
	public final static int DISPLAY_MODE = 1;
	public final static int EDIT_MODE = 2;
	public final static int ADD_MODE = 3;
	// search elements
	private String s_orderId;
	private String[] s_sites;
	private String[] s_status;
	private String[] s_itemTypes;
	private String s_name;
	private String s_itemId;
	private int s_stock;
	private int s_valueFrom;
	private int s_valueTo;
	private int s_svalueFrom;
	private int s_svalueTo;
	private Date s_cdateFrom;
	private Date s_cdateTo;
	private Date s_odateFrom;
	private Date s_odateTo;
	private int s_quantFrom;
	private int s_quantTo;
	private int s_squantFrom;
	private int s_squantTo;

	private static ArrayList<String[]> messages = new ArrayList<String[]>();
	private UploadedFile file;
	private StreamedContent pitemImage;
	
	private Pitem selectedItemForEdit;

	

	@PostConstruct
	public void init() {
		initDisplay();
		initSearch();
		pOrderDAOImp = (POrderDAOImp) context.getBean("orderDAO");
		pItemDAOImp = (PItemDAOImp) context.getBean("itemDAO");
	}

	public void initSearch() {
		s_orderId = "";
		s_sites = null;
		s_status = null;
		s_itemTypes = null;
		s_name= "";
		s_itemId = "";
		s_valueFrom = 0;
		s_valueTo = 100000;
		s_svalueFrom = 0;
		s_svalueTo = 100000;
		s_cdateFrom = null;
		s_cdateTo = null;
		s_odateFrom = null;
		s_odateTo = null;
		s_quantFrom = 0;
		s_quantTo = 1000;
		s_squantFrom = 0;
		s_squantTo = 1000;
	}

	public void initDisplay() {
	}

	private void clearPItem() {
		pitem = new Pitem();
	}


//	public void moveItemToAddMode() {
//		clearPItem();
//		pitem.setPorder(porder);
//		pitem.setCurrencyBean(porder.getCurrencyBean());
//		pitem.setSite(porder.getSite().getKey());
//		pitem.setOrderdate(porder.getOrderdate());
//		pitem.setSiteOrderId(porder.getOrderId());
//		idisplayMode = ADD_MODE;
//		file = null;
//		Map<String, Object> options = new HashMap<String, Object>();
//		options.put("resizable", true);
//		options.put("draggable", false);
//		options.put("modal", true);
//		options.put("minWidth", "1300");
//		options.put("minHeight", "900");
//		RequestContext.getCurrentInstance().openDialog("addPItem", options, null);
//	}
	
	public void moveItemToEditMode() {
		porder = pOrderDAOImp.getOrderById(pitem.getSiteOrderId());
		if(porder != null){
			pitem.setPorder(porder);
		}
		idisplayMode = EDIT_MODE;
		file = null;
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", false);
		options.put("modal", true);
		options.put("minWidth", "1300");
		options.put("minHeight", "900");
		//pitemImage = new DefaultStreamedContent(new ByteArrayInputStream(pitem.getImageBytes()), "image/jpg");
		RequestContext.getCurrentInstance().openDialog("addPItem", options, null);
	}
	
	
//	public void selectPitem() {
//		Pitem tempPitem = pOrderDAOImp.getOrderById(porder.getOrderId());
//		if(tempPorder!= null){
//			porder = tempPorder;
//			loadPItems(porder.getOrderId());
//			displayMode = DISPLAY_MODE;
//		}else{
//			flushMessages();
//			addMessage(new String[] { "Warn", "No order found with id "+ porder.getOrderId()});
//			showMessage();
//		}
//	}


	public void searchItems() {
		pitems = new DBTableDataModel<Pitem>(pItemDAOImp.searchItems(s_sites, s_orderId, s_status, s_itemTypes, s_valueFrom,
				s_valueTo, s_svalueFrom, s_svalueTo, s_name, s_cdateFrom != null ? s_cdateFrom : null, s_cdateTo != null ? s_cdateTo : null,
				s_odateFrom != null ? s_odateFrom : null, s_odateTo != null ? s_odateTo : null , s_quantFrom, s_quantTo, s_squantFrom, s_squantTo));
	}


	public void savePitem(SelectEvent event) {
		flushMessages();
		if (pitem != null) {
			if (idisplayMode == EDIT_MODE)// update
			{
				try {
					pItemDAOImp.updateItem(pitem);
					addMessage(new String[] { "Successful", "Item edit saved!" });
				} catch (Exception e) {
					addMessage(new String[] { "Error", e.getMessage() });
					e.printStackTrace();
				}
			} else if (idisplayMode == ADD_MODE) {// save new
				try {
					pItemDAOImp.insertItem(pitem);
					addMessage(new String[] { "Successful", "Item creation saved!" });
					idisplayMode = EDIT_MODE;
				} catch (SQLException e) {
					addMessage(new String[] { "Error", e.getMessage() });
					e.printStackTrace();
				}
			}
		}
		showMessage();
	}

	private void flushMessages() {
		messages = new ArrayList<String[]>();
	}

	public void addMessage(String[] message) {
		messages.add(message);
	}
	
	public void showMessage() {
		FacesContext context = FacesContext.getCurrentInstance();
		for (String[] s : messages) {
			if(s[0].equalsIgnoreCase("error"))
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, s[0], s[0] + " " + s[1]));
			else if(s[0].equalsIgnoreCase("warn"))
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, s[0], s[0] + " " + s[1]));
			else
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s[0], s[0] + " " + s[1]));

		}
	}

	public void closeDialog(Pitem pitem) {
		RequestContext.getCurrentInstance().closeDialog(pitem);
	}
	public void closeDialog() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}


	public String getSARValue() {
		if (porder != null && porder.getCurrencyBean() != null && porder.getValue().floatValue() > 0)
			return (porder.getValue().floatValue() * porder.getCurrencyBean().getToSarRate()) + " SAR";
		return " SAR";
	}



	public void adjustPorderCurrency() {
		porder.getCurrencyBean();
	}

	

	

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void handlePitemImage(FileUploadEvent event){
		file = event.getFile();
		pitem.setImageBytes(file.getContents());
		pitemImage = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), "image/jpg");
	}
	/**
	 * @return the pitemImage
	 */
	public StreamedContent getPitemImage() {
		if(file == null)
			pitemImage = new DefaultStreamedContent(new ByteArrayInputStream(pitem.getImageBytes()!=null?pitem.getImageBytes():new byte[0]), "image/jpg");
		return pitemImage;
	}

	/**
	 * @param pitemImage the pitemImage to set
	 */
	public void setPitemImage(StreamedContent pitemImage) {
		this.pitemImage = pitemImage;
	}
	
	public StreamedContent getSteamedItemImage(){
		
		FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
        	Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    		String pk = params.get("itemId");
    		if(pk == null) return null;
    		byte[] img = pItemDAOImp.getItemImageById(pk);
    		return new DefaultStreamedContent(new ByteArrayInputStream(img != null ? img : new byte[0]), "image/jpg");
        }
		
	}
	
	public void selectOrder(Pitem item){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("addOrder.xhtml?orderId="+item.getPorder().getOrderId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the pitems
	 */
	public DBTableDataModel<Pitem> getPitems() {
		return pitems;
	}

	/**
	 * @param pitems the pitems to set
	 */
	public void setPitems(DBTableDataModel<Pitem> pitems) {
		this.pitems = pitems;
	}

	/**
	 * @return the pitemsList
	 */
	public ArrayList<Pitem> getPitemsList() {
		return pitemsList;
	}

	/**
	 * @param pitemsList the pitemsList to set
	 */
	public void setPitemsList(ArrayList<Pitem> pitemsList) {
		this.pitemsList = pitemsList;
	}

	/**
	 * @return the porder
	 */
	public Porder getPorder() {
		return porder;
	}

	/**
	 * @param porder the porder to set
	 */
	public void setPorder(Porder porder) {
		this.porder = porder;
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
	 * @return the idisplayMode
	 */
	public int getIdisplayMode() {
		return idisplayMode;
	}

	/**
	 * @param idisplayMode the idisplayMode to set
	 */
	public void setIdisplayMode(int idisplayMode) {
		this.idisplayMode = idisplayMode;
	}

	/**
	 * @return the s_orderId
	 */
	public String getS_orderId() {
		return s_orderId;
	}

	/**
	 * @param s_orderId the s_orderId to set
	 */
	public void setS_orderId(String s_orderId) {
		this.s_orderId = s_orderId;
	}

	/**
	 * @return the s_sites
	 */
	public String[] getS_sites() {
		return s_sites;
	}

	/**
	 * @param s_sites the s_sites to set
	 */
	public void setS_sites(String[] s_sites) {
		this.s_sites = s_sites;
	}

	/**
	 * @return the s_status
	 */
	public String[] getS_status() {
		return s_status;
	}

	/**
	 * @param s_status the s_status to set
	 */
	public void setS_status(String[] s_status) {
		this.s_status = s_status;
	}

	/**
	 * @return the s_name
	 */
	public String getS_name() {
		return s_name;
	}

	/**
	 * @param s_name the s_name to set
	 */
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	/**
	 * @return the s_itemId
	 */
	public String getS_itemId() {
		return s_itemId;
	}

	/**
	 * @param s_itemId the s_itemId to set
	 */
	public void setS_itemId(String s_itemId) {
		this.s_itemId = s_itemId;
	}

	/**
	 * @return the s_stock
	 */
	public int getS_stock() {
		return s_stock;
	}

	/**
	 * @param s_stock the s_stock to set
	 */
	public void setS_stock(int s_stock) {
		this.s_stock = s_stock;
	}

	/**
	 * @return the s_valueFrom
	 */
	public int getS_valueFrom() {
		return s_valueFrom;
	}

	/**
	 * @param s_valueFrom the s_valueFrom to set
	 */
	public void setS_valueFrom(int s_valueFrom) {
		this.s_valueFrom = s_valueFrom;
	}

	/**
	 * @return the s_valueTo
	 */
	public int getS_valueTo() {
		return s_valueTo;
	}

	/**
	 * @param s_valueTo the s_valueTo to set
	 */
	public void setS_valueTo(int s_valueTo) {
		this.s_valueTo = s_valueTo;
	}

	/**
	 * @return the s_svalueFrom
	 */
	public int getS_svalueFrom() {
		return s_svalueFrom;
	}

	/**
	 * @param s_svalueFrom the s_svalueFrom to set
	 */
	public void setS_svalueFrom(int s_svalueFrom) {
		this.s_svalueFrom = s_svalueFrom;
	}

	/**
	 * @return the s_svalueTo
	 */
	public int getS_svalueTo() {
		return s_svalueTo;
	}

	/**
	 * @param s_svalueTo the s_svalueTo to set
	 */
	public void setS_svalueTo(int s_svalueTo) {
		this.s_svalueTo = s_svalueTo;
	}

	/**
	 * @return the s_cdateFrom
	 */
	public Date getS_cdateFrom() {
		return s_cdateFrom;
	}

	/**
	 * @param s_cdateFrom the s_cdateFrom to set
	 */
	public void setS_cdateFrom(Date s_cdateFrom) {
		this.s_cdateFrom = s_cdateFrom;
	}

	/**
	 * @return the s_cdateTo
	 */
	public Date getS_cdateTo() {
		return s_cdateTo;
	}

	/**
	 * @param s_cdateTo the s_cdateTo to set
	 */
	public void setS_cdateTo(Date s_cdateTo) {
		this.s_cdateTo = s_cdateTo;
	}

	/**
	 * @return the s_odateFrom
	 */
	public Date getS_odateFrom() {
		return s_odateFrom;
	}

	/**
	 * @param s_odateFrom the s_odateFrom to set
	 */
	public void setS_odateFrom(Date s_odateFrom) {
		this.s_odateFrom = s_odateFrom;
	}

	/**
	 * @return the s_odateTo
	 */
	public Date getS_odateTo() {
		return s_odateTo;
	}

	/**
	 * @param s_odateTo the s_odateTo to set
	 */
	public void setS_odateTo(Date s_odateTo) {
		this.s_odateTo = s_odateTo;
	}

	/**
	 * @return the s_quantFrom
	 */
	public int getS_quantFrom() {
		return s_quantFrom;
	}

	/**
	 * @param s_quantFrom the s_quantFrom to set
	 */
	public void setS_quantFrom(int s_quantFrom) {
		this.s_quantFrom = s_quantFrom;
	}

	/**
	 * @return the s_quantTo
	 */
	public int getS_quantTo() {
		return s_quantTo;
	}

	/**
	 * @param s_quantTo the s_quantTo to set
	 */
	public void setS_quantTo(int s_quantTo) {
		this.s_quantTo = s_quantTo;
	}

	/**
	 * @return the s_squantFrom
	 */
	public int getS_squantFrom() {
		return s_squantFrom;
	}

	/**
	 * @param s_squantFrom the s_squantFrom to set
	 */
	public void setS_squantFrom(int s_squantFrom) {
		this.s_squantFrom = s_squantFrom;
	}

	/**
	 * @return the s_squantTo
	 */
	public int getS_squantTo() {
		return s_squantTo;
	}

	/**
	 * @param s_squantTo the s_squantTo to set
	 */
	public void setS_squantTo(int s_squantTo) {
		this.s_squantTo = s_squantTo;
	}

	/**
	 * @return the messages
	 */
	public static ArrayList<String[]> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public static void setMessages(ArrayList<String[]> messages) {
		PItemHandler.messages = messages;
	}

	/**
	 * @return the tableView
	 */
	public boolean isTableView() {
		return tableView;
	}

	/**
	 * @param tableView the tableView to set
	 */
	public void setTableView(boolean tableView) {
		this.tableView = tableView;
	}
	
	public void moveToTableView(){
		tableView = true;
	}
	
	public void moveToPhotoView(){
		tableView = false;
	}

	/**
	 * @return the s_itemTypes
	 */
	public String[] getS_itemTypes() {
		return s_itemTypes;
	}

	/**
	 * @param s_itemTypes the s_itemTypes to set
	 */
	public void setS_itemTypes(String[] s_itemTypes) {
		this.s_itemTypes = s_itemTypes;
	}

	/**
	 * @return the selectedItemForEdit
	 */
	public Pitem getSelectedItemForEdit() {
		return selectedItemForEdit;
	}

	/**
	 * @param selectedItemForEdit the selectedItemForEdit to set
	 */
	public void setSelectedItemForEdit(Pitem selectedItemForEdit) {
		this.selectedItemForEdit = selectedItemForEdit;
	}
	
	
	
	
}
