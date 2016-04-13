/**
 * 
 */
package com.hammam.business.om.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import org.springframework.stereotype.Component;

import com.hammam.business.om.dao.PItemDAOImp;
import com.hammam.business.om.dao.POrderDAOImp;
import com.hammam.business.om.dao.PTrackingDAOImp;
import com.hammam.business.om.dao.SItemDAOImp;
import com.hammam.business.om.dao.SOrderDAOImp;
import com.hammam.business.om.jpa.Pitem;
import com.hammam.business.om.jpa.Porder;
import com.hammam.business.om.jpa.Ptracking;
import com.hammam.business.om.jpa.Sitem;
import com.hammam.business.om.jpa.Sorder;

/**
 * @author Hammam
 *
 */
@ManagedBean(name = "SOrderCtrl", eager = true)
@SessionScoped
@Component(value = "SOrderCtrl")
public class SorderHandler extends AbstractHandler {

	private SOrderDAOImp sOrderDAOImp;
	private SItemDAOImp sItemDAOImp;
	private PItemDAOImp pItemDAOImp;
	private DBTableDataModel<Sorder> sorders;
	private ArrayList<Sorder> sordersList;
	public static HashMap<String, Sorder> sordersMap;

	private Sorder sorder;
	private Sitem sitem;
	private int displayMode;
	private int idisplayMode;
	public final static int DISPLAY_MODE = 1;
	public final static int EDIT_MODE = 2;
	public final static int ADD_MODE = 3;

	// search elements
	private String s_customerName;
	private String[] s_delivery;
	private String[] s_status;
	private int s_valueFrom;
	private int s_valueTo;
	private Date s_cdateFrom;
	private Date s_cdateTo;
	private String s_contact;

	private static ArrayList<String[]> messages = new ArrayList<String[]>();

	private boolean detailsShowed;
	private Pitem selectedItemForView;
	private Sitem selectedItemForEdit;
	private Sitem selectedItemForDelete;
	private List<Pitem> availableItems;
	private String[] s_itemTypes;
	private static HashMap<String, byte[]> ITEMS_PHOTOS_MAP = new HashMap<String, byte[]>();

	@PostConstruct
	public void init() {
		ITEMS_PHOTOS_MAP = new HashMap<String, byte[]>();
		initDisplay();
		initSearch();
		sOrderDAOImp = (SOrderDAOImp) context.getBean("sorderDAO");
		sItemDAOImp = (SItemDAOImp) context.getBean("sitemDAO");
		pItemDAOImp = (PItemDAOImp) context.getBean("itemDAO");
		initSorder();
	}

	
	private void initSorder() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String pk = params.get("seqId");
		if(pk!= null && pk.equals("-1")){
			moveToNewOrderMode();
			return;
		}
		sorder = sOrderDAOImp.getOrderBySeqId(pk);
		if (sorder != null)
			moveOrderToEditMode();
		else
			moveToNewOrderMode();
	}

	public void initSearch() {
		s_customerName = "";
		s_delivery = null;
		s_status = null;
		s_valueFrom = 0;
		s_valueTo = 100000;
		s_cdateFrom = null;
		s_cdateTo = null;
		s_contact = "";
	}

	public void initDisplay() {
		detailsShowed = true;
		moveToNewOrderMode();
		
	}

	private void clearSItem() {
		sitem = new Sitem();
	}

	public void clearSOrder() {
		sorder = new Sorder();
		sorder.setSitems(new ArrayList<Sitem>());
	}

	public void moveToNewOrderMode() {
		detailsShowed = true;
		clearSOrder();
		clearSItem();
		displayMode = ADD_MODE;
	}

	public void moveToEditMode() {
		displayMode = EDIT_MODE;
	}

	public void moveItemToAddMode() {
		clearSItem();
		sitem.setSorder(sorder);
		idisplayMode = ADD_MODE;
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", false);
		options.put("modal", true);
		options.put("minWidth", "1300");
		options.put("minHeight", "900");
		RequestContext.getCurrentInstance().openDialog("addSItem", options, null);
	}

	public void executeSpinner(int value){
		System.out.println(value);
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String pk = params.get("itemId");
		int sq = Integer.parseInt(params.get("sq"));
		flushMessages();
		try {
			sItemDAOImp.addItemToOrder(sorder, value, pk, (sq-value));
			addMessage(new String[] { "Successful", "Item Added !" });
			noavialble();
		} catch (Exception e) {
			addMessage(new String[] { "Error", e.getMessage() });
			e.printStackTrace();
		}
		loadSItems(sorder.getSeqId());
		// RequestContext.getCurrentInstance().closeDialog(pitem);
		showMessage();
	}
	public void saveSorder() {
		flushMessages();
		if (sorder != null) {
			if (displayMode == EDIT_MODE)// update
			{
				try {
					String msg = sOrderDAOImp.updateOrder(sorder);
					addMessage(new String[] { "Successful", "Ordered edit saved!" });
					showMessage();

				} catch (Exception e) {
					addMessage(new String[] { "Error", e.getMessage() });
					showMessage();
					e.printStackTrace();
					return;
				}
			} else if (displayMode == ADD_MODE) {// save new
				try {
					sOrderDAOImp.insertOrder(sorder);
					addMessage(new String[] { "Successful", "Ordered creation saved!" });
					showMessage();
					displayMode = EDIT_MODE;
				} catch (SQLException e) {
					addMessage(new String[] { "Error", e.getMessage() });
					showMessage();
					e.printStackTrace();
					return;
				}
			}
			loadSItems(sorder.getSeqId());
		}
	}

	public void saveSitem(SelectEvent event) {
		flushMessages();
		if (sitem != null) {
			if (idisplayMode == EDIT_MODE)// update
			{
				try {
					String msg = sItemDAOImp.updateItem(sitem);
					addMessage(new String[] { "Successful", "Item edit saved!" });
				} catch (Exception e) {
					addMessage(new String[] { "Error", e.getMessage() });
					e.printStackTrace();
				}
			} else if (idisplayMode == ADD_MODE) {// save new
				try {
					sItemDAOImp.insertItem(sitem);
					addMessage(new String[] { "Successful", "Item creation saved!" });
					// showMessage();
					idisplayMode = EDIT_MODE;
				} catch (SQLException e) {
					addMessage(new String[] { "Error", e.getMessage() });
					// showMessage();
					e.printStackTrace();
				}
			}
			
		}
		loadSItems(sorder.getSeqId());
		// RequestContext.getCurrentInstance().closeDialog(pitem);
		showMessage();
		
	}

	private void loadSItems(int id) {
		sorder.setSitems(sItemDAOImp.listItemsOfOrder(id));
		if (sorder.getSitems() != null)
			sorder.setTotalDiscount(0.0f);
			sorder.setTotalValue(0.0f);
			sorder.setNoItems(0);
			for (Sitem i : sorder.getSitems()){
				i.setSorder(sorder);
				i.setPitem(pItemDAOImp.getItemById(i.getPitem_seqId()+""));
				sorder.setTotalDiscount(sorder.getTotalDiscount() + i.getDiscount());
				sorder.setTotalValue(sorder.getTotalValue() + i.getTotalValueWD());
				sorder.setNoItems(sorder.getNoItems() + i.getQuantity());
				
				
			}

	}
	
	public void searchOrders() {
		sorders = new DBTableDataModel<Sorder>(sOrderDAOImp.searchOrderes(s_customerName, s_contact, s_status, s_valueFrom,
				s_valueTo, s_cdateFrom != null ? s_cdateFrom : null, s_cdateTo != null ? s_cdateTo : null,
				s_delivery));
	}
	
	public void onRowToggle(ToggleEvent event) {
		System.out.println(event.getVisibility());
		if (event.getVisibility().compareTo(Visibility.VISIBLE) == 0) {
			Sorder so = (Sorder) event.getData();
			so.setSitems(sItemDAOImp.listItemsOfOrder(so.getSeqId()));
		}

		//FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public List<Sorder> completeCustomerName(String query){
//		List<String> results = new ArrayList<String>();
		
		sordersList = (ArrayList<Sorder>) sOrderDAOImp.getActiveOrderByCustomerId(query );
		sordersMap = new HashMap<String, Sorder>();
		for(Sorder s : sordersList){
			sordersMap.put(s.getCustomerName(), s);
		}
//        for(Sorder order  : orders) {
//            results.add(order.getCustomerName());
//        }
//         
        return sordersList;
	}


	public void moveOrderToEditMode(SelectEvent event) {
		detailsShowed = false;

		displayMode = EDIT_MODE;
		sorder.setSitems(sItemDAOImp.listItemsOfOrder(sorder.getSeqId()));
		if (sorder.getSitems() != null)
			for (Sitem i : sorder.getSitems())
				i.setSorder(sorder);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("addSOrder.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void moveOrderToEditMode() {
		detailsShowed = false;

		displayMode = EDIT_MODE;
		sorder.setSitems(sItemDAOImp.listItemsOfOrder(sorder.getSeqId()));
		if (sorder.getSitems() != null)
			for (Sitem i : sorder.getSitems())
				i.setSorder(sorder);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("addSOrder.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void saveEditItem(){
		if(selectedItemForEdit != null){
			flushMessages();

			try {
				sItemDAOImp.addItemToOrder(selectedItemForEdit);
				addMessage(new String[] { "Successful", "Item edit saved!" });
			} catch (Exception e) {
				addMessage(new String[] { "Error", e.getMessage() });
				e.printStackTrace();
			}
			loadSItems(sorder.getSeqId());
			showMessage();
		}
	}
	
	public void deleteItemFromOrder(){
		if(selectedItemForDelete != null){
			flushMessages();
			try {
				sItemDAOImp.removeItemFromOrder(selectedItemForDelete);
				addMessage(new String[] { "Successful", "Item deletion saved!" });
			} catch (Exception e) {
				addMessage(new String[] { "Error", e.getMessage() });
				e.printStackTrace();
			}
			loadSItems(sorder.getSeqId());
			showMessage();
		}
		
	}
	
	public void selectSorder() {
		Sorder tempSorder = sOrderDAOImp.getOrderBySeqId(""+sorder.getSeqId());
		if (tempSorder != null) {
			sorder = tempSorder;
			loadSItems(sorder.getSeqId());
			if(displayMode == DISPLAY_MODE)
				displayMode = DISPLAY_MODE;
			else if(displayMode == EDIT_MODE)
				displayMode = EDIT_MODE;
			else if(displayMode == ADD_MODE)
				displayMode = EDIT_MODE;
				
				
		} else {
			flushMessages();
			addMessage(new String[] { "Warn", "No order found with id " + sorder.getCustomerName() });
			showMessage();
		}
	}

	public void closeDialog(Sitem sitem) {
		RequestContext.getCurrentInstance().closeDialog(sitem);
	}

	public void closeDialog() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	/**
	 * @return the sorders
	 */
	public DBTableDataModel<Sorder> getSorders() {
		return sorders;
	}

	/**
	 * @param sorders
	 *            the sorders to set
	 */
	public void setSorders(DBTableDataModel<Sorder> sorders) {
		this.sorders = sorders;
	}

	/**
	 * @return the sordersList
	 */
	public ArrayList<Sorder> getSordersList() {
		return sordersList;
	}

	/**
	 * @param sordersList
	 *            the sordersList to set
	 */
	public void setSordersList(ArrayList<Sorder> sordersList) {
		this.sordersList = sordersList;
	}

	/**
	 * @return the sorder
	 */
	public Sorder getSorder() {
		return sorder;
	}

	/**
	 * @param sorder
	 *            the sorder to set
	 */
	public void setSorder(Sorder sorder) {
		this.sorder = sorder;
	}

	/**
	 * @return the sitem
	 */
	public Sitem getSitem() {
		return sitem;
	}

	/**
	 * @param sitem
	 *            the sitem to set
	 */
	public void setSitem(Sitem sitem) {
		this.sitem = sitem;
	}

	/**
	 * @return the displayMode
	 */
	public int getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode
	 *            the displayMode to set
	 */
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the idisplayMode
	 */
	public int getIdisplayMode() {
		return idisplayMode;
	}

	/**
	 * @param idisplayMode
	 *            the idisplayMode to set
	 */
	public void setIdisplayMode(int idisplayMode) {
		this.idisplayMode = idisplayMode;
	}

	/**
	 * @return the s_customerName
	 */
	public String getS_customerName() {
		return s_customerName;
	}

	/**
	 * @param s_customerName
	 *            the s_customerName to set
	 */
	public void setS_customerName(String s_customerName) {
		this.s_customerName = s_customerName;
	}

	/**
	 * @return the s_delivery
	 */
	public String[] getS_delivery() {
		return s_delivery;
	}

	/**
	 * @param s_delivery
	 *            the s_delivery to set
	 */
	public void setS_delivery(String[] s_delivery) {
		this.s_delivery = s_delivery;
	}

	/**
	 * @return the s_status
	 */
	public String[] getS_status() {
		return s_status;
	}

	/**
	 * @param s_status
	 *            the s_status to set
	 */
	public void setS_status(String[] s_status) {
		this.s_status = s_status;
	}

	/**
	 * @return the s_valueFrom
	 */
	public int getS_valueFrom() {
		return s_valueFrom;
	}

	/**
	 * @param s_valueFrom
	 *            the s_valueFrom to set
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
	 * @param s_valueTo
	 *            the s_valueTo to set
	 */
	public void setS_valueTo(int s_valueTo) {
		this.s_valueTo = s_valueTo;
	}

	/**
	 * @return the s_cdateFrom
	 */
	public Date getS_cdateFrom() {
		return s_cdateFrom;
	}

	/**
	 * @param s_cdateFrom
	 *            the s_cdateFrom to set
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
	 * @param s_cdateTo
	 *            the s_cdateTo to set
	 */
	public void setS_cdateTo(Date s_cdateTo) {
		this.s_cdateTo = s_cdateTo;
	}

	/**
	 * @return the s_contact
	 */
	public String getS_contact() {
		return s_contact;
	}

	/**
	 * @param s_contact
	 *            the s_contact to set
	 */
	public void setS_contact(String s_contact) {
		this.s_contact = s_contact;
	}

	/**
	 * @return the messages
	 */
	public static ArrayList<String[]> getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public static void setMessages(ArrayList<String[]> messages) {
		SorderHandler.messages = messages;
	}

	/**
	 * @return the detailsShowed
	 */
	public boolean isDetailsShowed() {
		return detailsShowed;
	}

	/**
	 * @param detailsShowed
	 *            the detailsShowed to set
	 */
	public void setDetailsShowed(boolean detailsShowed) {
		this.detailsShowed = detailsShowed;
	}
	
	

	/**
	 * @return the selectedItemForView
	 */
	public Pitem getSelectedItemForView() {
		return selectedItemForView;
	}

	/**
	 * @param selectedItemForView the selectedItemForView to set
	 */
	public void setSelectedItemForView(Pitem selectedItemForView) {
		this.selectedItemForView = selectedItemForView;
	}
	
	

	/**
	 * @return the selectedItemForEdit
	 */
	public Sitem getSelectedItemForEdit() {
		return selectedItemForEdit;
	}


	/**
	 * @param selectedItemForEdit the selectedItemForEdit to set
	 */
	public void setSelectedItemForEdit(Sitem selectedItemForEdit) {
		this.selectedItemForEdit = selectedItemForEdit;
	}

	

	/**
	 * @return the selectedItemForDelete
	 */
	public Sitem getSelectedItemForDelete() {
		return selectedItemForDelete;
	}


	/**
	 * @param selectedItemForDelete the selectedItemForDelete to set
	 */
	public void setSelectedItemForDelete(Sitem selectedItemForDelete) {
		this.selectedItemForDelete = selectedItemForDelete;
	}


	/**
	 * @return the availableItems
	 */
	public List<Pitem> getAvailableItems() {
		if(availableItems == null){
			System.out.println("before ");
			availableItems = pItemDAOImp.listAvailableItemsByType(s_itemTypes);
			System.out.println("after ");
		}
			
		if(availableItems != null){
			for(Pitem pitem : availableItems){
				ITEMS_PHOTOS_MAP.put(pitem.getSeqId()+"", pitem.getImageBytes());
			}
		}
		return availableItems;
	}

	/**
	 * @param availableItems the availableItems to set
	 */
	public void setAvailableItems(List<Pitem> availableItems) {
		this.availableItems = availableItems;
	}

	public boolean isDeliverCostRequired() {
		if (sorder == null)
			return false;
		else {
			if (sorder.getDeliveryTypeBean() != null && sorder.getDeliveryTypeBean().getKey().equals("To Home"))
				return true;
			else
				return false;
		}
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
			if (s[0].equalsIgnoreCase("error"))
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, s[0], s[0] + " " + s[1]));
			else if (s[0].equalsIgnoreCase("warn"))
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, s[0], s[0] + " " + s[1]));
			else
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s[0], s[0] + " " + s[1]));

		}
	}
	
	public void nothing(){}
	
	public void noavialble(){
		availableItems = pItemDAOImp.listAvailableItemsByType(s_itemTypes);
		
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
			byte[] img = new byte[0];
			if(ITEMS_PHOTOS_MAP.containsKey(pk))
				img = ITEMS_PHOTOS_MAP.get(pk);
			else{
				img = pItemDAOImp.getItemImageById(pk);
				ITEMS_PHOTOS_MAP.put(pk, img);
			}
			return new DefaultStreamedContent(new ByteArrayInputStream(img != null ? img : new byte[0]), "image/jpg");
	    }
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
	
	public void loadItemsofType(){
		
	}
	
}
