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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;
import org.springframework.stereotype.Component;

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
@ManagedBean(name = "POrderCtrl", eager = true)
@SessionScoped
@Component(value = "POrderCtrl")
public class POrderHandler extends AbstractHandler implements Serializable {

	private static final long serialVersionUID = 1L;
	private POrderDAOImp pOrderDAOImp;
	private PItemDAOImp pItemDAOImp;
	private PTrackingDAOImp pTrackingDAOImp;
	private DBTableDataModel<Porder> porders;
	private ArrayList<Porder> pordersList;

	private Porder porder;
	private Pitem pitem;
	private Ptracking ptracking;
	private int displayMode;
	private int idisplayMode;
	private int tdisplayMode;
	public final static int DISPLAY_MODE = 1;
	public final static int EDIT_MODE = 2;
	public final static int ADD_MODE = 3;
	private static byte[] testbte;
	// search elements
	private String s_orderId;
	private String[] s_sites;
	private String[] s_status;
	private int s_valueFrom;
	private int s_valueTo;
	private Date s_cdateFrom;
	private Date s_cdateTo;
	private Date s_odateFrom;
	private Date s_odateTo;
	private Currency sCurrency;

	private static ArrayList<String[]> messages = new ArrayList<String[]>();
	private Panel itemsPanel;
	private UploadedFile file;
	private StreamedContent pitemImage;

	public Currency getsCurrency() {
		return sCurrency;
	}

	public void setsCurrency(Currency sCurrency) {
		this.sCurrency = sCurrency;
	}

	@PostConstruct
	public void init() {
		initDisplay();
		initSearch();
		pOrderDAOImp = (POrderDAOImp) context.getBean("orderDAO");
		pItemDAOImp = (PItemDAOImp) context.getBean("itemDAO");
		initPorder();
	}

	private void initPorder() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String pk = params.get("orderId");
		porder = pOrderDAOImp.getOrderById(pk);
		if(porder != null)
			moveOrderToEditMode();
		else
			moveToNewOrderMode();
	}

	public void initSearch() {
		s_orderId = "";
		s_sites = null;
		s_status = null;
		s_valueFrom = 0;
		s_valueTo = 100000;
		s_cdateFrom = null;
		s_cdateTo = null;
		s_odateFrom = null;
		s_odateTo = null;

	}

	public void initDisplay() {
		moveToNewOrderMode();
	}

	private void clearPItem() {
		pitem = new Pitem();
	}

	public void moveToNewOrderMode() {
		clearPOrder();
		clearPItem();
		clearPTracking();
		displayMode = ADD_MODE;
	}

	public void moveToEditMode() {
		displayMode = EDIT_MODE;
	}

	public void prepareItemForEdit() {

	}

	public void moveToAddTrackingMode() {
		clearPTracking();
		ptracking.setPorder(porder);
	}

	public void moveItemToAddMode() {
		clearPItem();
		pitem.setPorder(porder);
		pitem.setCurrencyBean(porder.getCurrencyBean());
		pitem.setSite(porder.getSite().getKey());
		pitem.setOrderdate(porder.getOrderdate());
		pitem.setSiteOrderId(porder.getOrderId());
		idisplayMode = ADD_MODE;
		file = null;
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", false);
		options.put("modal", true);
		options.put("minWidth", "1300");
		options.put("minHeight", "900");
		RequestContext.getCurrentInstance().openDialog("addPItem", options, null);
	}

	public void moveItemToEditMode() {
		System.out.println("before show pitem.getImageBytes().length = " + pitem.getImageBytes().length);
		if (Arrays.equals(pitem.getImageBytes(), testbte)) {
			System.out.println("Yup, they're the same!");
		} else
			System.out.println("Not the same :((((((((((");
		pitem.setPorder(porder);
		pitem.setCurrencyBean(porder.getCurrencyBean());
		pitem.setSite(porder.getSite().getKey());
		pitem.setOrderdate(porder.getOrderdate());
		pitem.setSiteOrderId(porder.getOrderId());
		idisplayMode = EDIT_MODE;
		file = null;
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", false);
		options.put("modal", true);
		options.put("minWidth", "1300");
		options.put("minHeight", "900");
		// pitemImage = new DefaultStreamedContent(new
		// ByteArrayInputStream(pitem.getImageBytes()), "image/jpg");
		RequestContext.getCurrentInstance().openDialog("addPItem", options, null);
	}

	public void moveToEditOrderMode() {
		displayMode = EDIT_MODE;
	}

	public void selectPorder() {
		Porder tempPorder = pOrderDAOImp.getOrderById(porder.getOrderId());
		if (tempPorder != null) {
			porder = tempPorder;
			loadPItems(porder.getOrderId());
			displayMode = DISPLAY_MODE;
		} else {
			flushMessages();
			addMessage(new String[] { "Warn", "No order found with id " + porder.getOrderId() });
			showMessage();
		}
	}

	private void clearPTracking() {
		ptracking = new Ptracking();

	}

	public void searchOrders() {
		porders = new DBTableDataModel<Porder>(pOrderDAOImp.searchOrderes(s_sites, s_orderId, s_status, s_valueFrom,
				s_valueTo, s_cdateFrom != null ? s_cdateFrom : null, s_cdateTo != null ? s_cdateTo : null,
				s_odateFrom != null ? s_odateFrom : null, s_odateTo != null ? s_odateTo : null));
	}

	public void loadPorders() {
		porders = new DBTableDataModel<Porder>(getAllOrders());
	}

	private List<Porder> getAllOrders() {
		return pOrderDAOImp.listOrderes();

	}

	public void clearPOrder() {
		porder = new Porder();
		porder.setPitems(new ArrayList<Pitem>());
		porder.setPtrackings(new ArrayList<Ptracking>());
	}

	public void getPorder(String id) {
		porder = pOrderDAOImp.getOrderById(id);
		loadPItems(id);
	}

	private void loadPItems(String id) {
		porder.setPitems(pItemDAOImp.listItemsOfOrder(id));
		if (porder.getPitems() != null)
			for (Pitem i : porder.getPitems())
				i.setPorder(porder);

	}

	/**
	 * @return the porders
	 */
	public DBTableDataModel<Porder> getPorders() {
		return porders;
	}

	public void savePorder() {
		flushMessages();
		if (porder != null) {
			if (displayMode == EDIT_MODE)// update
			{
				try {
					String msg = pOrderDAOImp.updateOrder(porder);
					addMessage(new String[] { "Successful", "Ordered edit saved!" });
					showMessage();

				} catch (Exception e) {
					addMessage(new String[] { "Error", e.getMessage() });
					showMessage();
					e.printStackTrace();
				}
			} else if (displayMode == ADD_MODE) {// save new
				try {
					// try {
					// Thread.sleep(10000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					pOrderDAOImp.insertOrder(porder);
					addMessage(new String[] { "Successful", "Ordered creation saved!" });
					showMessage();
					displayMode = EDIT_MODE;
				} catch (SQLException e) {
					addMessage(new String[] { "Error", e.getMessage() });
					showMessage();
					e.printStackTrace();
				}
			}
		}
	}

	public void savePitem(SelectEvent event) {
		flushMessages();
		if (pitem != null) {
			if (idisplayMode == EDIT_MODE)// update
			{
				try {
					System.out.println("before update pitem.getImageBytes().length = " + pitem.getImageBytes().length);

					String msg = pItemDAOImp.updateItem(pitem);
					addMessage(new String[] { "Successful", "Item edit saved!" });
					// showMessage();
					System.out.println("after update pitem.getImageBytes().length = " + pitem.getImageBytes().length);
				} catch (Exception e) {
					addMessage(new String[] { "Error", e.getMessage() });
					// showMessage();
					e.printStackTrace();
				}
			} else if (idisplayMode == ADD_MODE) {// save new
				try {
					// try {
					// Thread.sleep(10000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					pItemDAOImp.insertItem(pitem);
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
		loadPItems(porder.getOrderId());
		// RequestContext.getCurrentInstance().closeDialog(pitem);
		showMessage();
	}

	private void flushMessages() {
		messages = new ArrayList<String[]>();
	}

	public void moveOrderToEditMode(SelectEvent event) {
		displayMode = EDIT_MODE;
		porder.setPitems(pItemDAOImp.listItemsOfExactOrder(porder.getOrderId()));
		if (porder.getPitems() != null)
			for (Pitem i : porder.getPitems())
				i.setPorder(porder);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("addOrder.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void moveOrderToEditMode() {
		displayMode = EDIT_MODE;
		porder.setPitems(pItemDAOImp.listItemsOfExactOrder(porder.getOrderId()));
		if (porder.getPitems() != null)
			for (Pitem i : porder.getPitems())
				i.setPorder(porder);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("addOrder.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void closeDialog(Pitem pitem) {
		RequestContext.getCurrentInstance().closeDialog(pitem);
	}

	public void closeDialog() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	/**
	 * @return the itemsPanel
	 */
	// public Panel getItemsPanel() {
	//
	// itemsPanel = createPanel();
	// return itemsPanel;
	// }

	// private Panel createPanel() {
	// FacesContext fc = FacesContext.getCurrentInstance();
	// Application application = fc.getApplication();
	//
	// Panel panel = (Panel) application.createComponent(Panel.COMPONENT_TYPE);
	// if (porder != null)
	// for (Pitem pitem : porder.getPitems()) {
	// Fieldset fs = createItemFieldset(pitem);
	// panel.getChildren().add(fs);
	// }
	// return panel;
	//
	// }

	/**
	 * @param itemsPanel
	 *            the itemsPanel to set
	 */
	public void setItemsPanel(Panel itemsPanel) {
		this.itemsPanel = itemsPanel;
	}

	// private Fieldset createItemFieldset(Pitem item) {
	// FacesContext fc = FacesContext.getCurrentInstance();
	// Application application = fc.getApplication();
	// ExpressionFactory ef = application.getExpressionFactory();
	// ELContext elc = fc.getELContext();
	//
	// Fieldset fs = (Fieldset)
	// application.createComponent(Fieldset.COMPONENT_TYPE);
	// fs.setToggleable(true);
	// fs.setToggleSpeed(100);
	// fs.setLegend(item.getName());
	// PanelGrid pg = (PanelGrid)
	// application.createComponent(PanelGrid.COMPONENT_TYPE);
	// fs.getChildren().add(pg);
	// //pg.setColumns(2);
	// pg.setStyle("border:0px; width:100%;");
	//
	// Row r1 = (Row) application.createComponent(Row.COMPONENT_TYPE);
	// r1.setStyle("border:0px; width:100%;");
	// pg.getChildren().add(r1);
	//
	// Column c1 = (Column) application.createComponent(Column.COMPONENT_TYPE);
	// c1.setStyle("border:0px;");
	// r1.getChildren().add(c1);
	//
	// GraphicImage gi = (GraphicImage)
	// application.createComponent(GraphicImage.COMPONENT_TYPE);
	// gi.setHeight("100");
	// gi.setValue(new DefaultStreamedContent(new
	// ByteArrayInputStream(pitem.getImageBytes()), "image/jpg"));
	// gi.setWidth("100");
	// // TODO add image
	// ValueExpression imageve =ef.createValueExpression(elc,
	// "#{POrderCtrl.teamedItemImage(\""+item.getSeqId()+"\")}",
	// DefaultStreamedContent.class);
	//
	// UIParameter param = new UIParameter();
	// param.setName("pk");
	// param.setValue(item.getSeqId());
	// gi.getChildren().add(param);
	// FacesContext context = FacesContext.getCurrentInstance();
	//
	// gi.setValueExpression("value", imageve);
	//
	//
	// c1.getChildren().add(gi);
	//
	// Column c2 = (Column) application.createComponent(Column.COMPONENT_TYPE);
	// c2.setStyle("border:0px;");
	// r1.getChildren().add(c2);
	// c2.setColspan(2);
	//
	// PanelGrid pg2 = (PanelGrid)
	// application.createComponent(PanelGrid.COMPONENT_TYPE);
	// c2.getChildren().add(pg2);
	// pg2.setColumns(2);
	// pg2.setStyle("border:0px; width:100%;");
	// pg2.setColumnClasses("_50, _50");
	//
	// OutputLabel ol4 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol4.setValue("Id: " + item.getSeqId());
	// pg2.getChildren().add(ol4);
	//
	// OutputLabel ol1 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol1.setValue("Status: " + item.getPitemStatus());
	// pg2.getChildren().add(ol1);
	//
	// OutputLabel ol2 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol2.setValue("Value / Selling: " + item.getValue() + " / " +
	// item.getSellingValue());
	// pg2.getChildren().add(ol2);
	//
	// OutputLabel ol3 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol3.setValue("Quantity / Stock: " + item.getDeliveredQuantity() + " / " +
	// item.getQuantityInStock());
	// pg2.getChildren().add(ol3);
	//
	// OutputLabel ol5 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol5.setValue("Discount Allowed: " + item.getMaxDiscount());
	// pg2.getChildren().add(ol5);
	//
	// OutputLabel ol6 = (OutputLabel)
	// application.createComponent(OutputLabel.COMPONENT_TYPE);
	// ol6.setValue("URL: " + item.getUrl());
	// pg2.getChildren().add(ol6);
	//
	// return fs;
	// }

	/**
	 * @param porders
	 *            the porders to set
	 */
	public void setPorders(DBTableDataModel<Porder> porders) {
		this.porders = porders;
	}

	/**
	 * @return the pordersList
	 */
	public ArrayList<Porder> getPordersList() {
		return pordersList;
	}

	/**
	 * @param pordersList
	 *            the pordersList to set
	 */
	public void setPordersList(ArrayList<Porder> pordersList) {
		this.pordersList = pordersList;
	}

	/**
	 * @return the porder
	 */
	public Porder getPorder() {
		return porder;
	}

	/**
	 * @param porder
	 *            the porder to set
	 */
	public void setPorder(Porder porder) {
		this.porder = porder;
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
	 * @return the s_orderId
	 */
	public String getS_orderId() {
		return s_orderId;
	}

	/**
	 * @param s_orderId
	 *            the s_orderId to set
	 */
	public void setS_orderId(String s_orderId) {
		this.s_orderId = s_orderId;
	}

	/**
	 * @return the pitem
	 */
	public Pitem getPitem() {
		return pitem;
	}

	/**
	 * @param pitem
	 *            the pitem to set
	 */
	public void setPitem(Pitem pitem) {
		this.pitem = pitem;
	}

	/**
	 * @return the ptracking
	 */
	public Ptracking getPtracking() {
		return ptracking;
	}

	/**
	 * @param ptracking
	 *            the ptracking to set
	 */
	public void setPtracking(Ptracking ptracking) {
		this.ptracking = ptracking;
	}

	/**
	 * @return the s_sites
	 */
	public String[] getS_sites() {
		return s_sites;
	}

	/**
	 * @param s_sites
	 *            the s_sites to set
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
	 * @return the s_odateFrom
	 */
	public Date getS_odateFrom() {
		return s_odateFrom;
	}

	/**
	 * @param s_odateFrom
	 *            the s_odateFrom to set
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
	 * @param s_odateTo
	 *            the s_odateTo to set
	 */
	public void setS_odateTo(Date s_odateTo) {
		this.s_odateTo = s_odateTo;
	}

	public String getSARValue() {
		if (porder != null && porder.getCurrencyBean() != null && porder.getValue().floatValue() > 0)
			return (porder.getValue().floatValue() * porder.getCurrencyBean().getToSarRate() * porder.getEFC()) + " SAR";
		return " SAR";
	}

	/**
	 * @return the messages
	 */
	public ArrayList<String[]> getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(ArrayList<String[]> messages) {
		this.messages = messages;
	}

	/**
	 * @return the editMode
	 */
	public static int getEditMode() {
		return EDIT_MODE;
	}

	/**
	 * @return the addMode
	 */
	public static int getAddMode() {
		return ADD_MODE;
	}

	public void adjustPorderCurrency() {
		porder.getCurrencyBean();
	}

	public void addMessage(String[] message) {
		messages.add(message);
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
	 * @return the tdisplayMode
	 */
	public int getTdisplayMode() {
		return tdisplayMode;
	}

	/**
	 * @param tdisplayMode
	 *            the tdisplayMode to set
	 */
	public void setTdisplayMode(int tdisplayMode) {
		this.tdisplayMode = tdisplayMode;
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

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void handlePitemImage(FileUploadEvent event) {
		file = event.getFile();
		pitem.setImageBytes(file.getContents());
		testbte = file.getContents();
		System.out.println(testbte.length);
		pitemImage = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), "image/jpg");
	}

	/**
	 * @return the pitemImage
	 */
	public StreamedContent getPitemImage() {
		// try {
		//
		// FacesContext context = FacesContext.getCurrentInstance();
		//
		// if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
		// // So, we're rendering the HTML. Return a stub StreamedContent so
		// that it will generate right URL.
		// return new DefaultStreamedContent();
		// }
		// else {
		// // So, browser is requesting the image. Return a real StreamedContent
		// with the image bytes.
		// if(pitem!=null && pitem.getImageBytes()!= null)
		// pitemImage = new DefaultStreamedContent(new
		// ByteArrayInputStream(pitem.getImageBytes()), "image/jpg");
		// else
		// if(pitemImage == null)
		// pitemImage = new DefaultStreamedContent(new ByteArrayInputStream(new
		// byte[0]));
		//
		// }
		//
		//
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		if (file == null)
			pitemImage = new DefaultStreamedContent(
					new ByteArrayInputStream(pitem.getImageBytes() != null ? pitem.getImageBytes() : new byte[0]),
					"image/jpg");
		return pitemImage;
	}

	/**
	 * @param pitemImage
	 *            the pitemImage to set
	 */
	public void setPitemImage(StreamedContent pitemImage) {
		this.pitemImage = pitemImage;
	}

	public StreamedContent getSteamedItemImage() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String pk = params.get("itemId");
		System.out.println(params.keySet().toArray());
		System.out.println(pk);
		byte[] img = porder.getItemsPhotosMap().get(pk);
		System.out.println(img);
		System.out.println(porder.getItemsPhotosMap().containsKey(pk));
		return new DefaultStreamedContent(new ByteArrayInputStream(img != null ? img : new byte[0]), "image/jpg");
	}
	
	public StreamedContent getSteamedLiveItemImage() {
		
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
		
		
//		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		String pk = params.get("itemId");
//		if(pk == null) return null;
//		byte[] img = pItemDAOImp.getItemImageById(pk);
//		return new DefaultStreamedContent(new ByteArrayInputStream(img != null ? img : new byte[0]), "image/jpg");
	}

	public void onRowToggle(ToggleEvent event) {
		System.out.println(event.getVisibility());
		if (event.getVisibility().compareTo(Visibility.VISIBLE) == 0) {
			Porder po = (Porder) event.getData();
			po.setPitems(pItemDAOImp.listItemsOfExactOrder(po.getOrderId()));
		}

		//FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
