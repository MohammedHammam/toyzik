/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.primefaces.push.annotation.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.hammam.business.om.jpa.Carrier;
import com.hammam.business.om.jpa.Currency;
import com.hammam.business.om.jpa.DeliveryType;
import com.hammam.business.om.jpa.ItemType;
import com.hammam.business.om.jpa.PitemStatus;
import com.hammam.business.om.jpa.PorderStatus;
import com.hammam.business.om.jpa.PtrackingStatus;
import com.hammam.business.om.jpa.Site;
import com.hammam.business.om.jpa.SorderStatus;

/**
 * @author Hammam
 *
 */
@Service(value = "lovsDAO")
@Singleton
public class LOVsDAO extends JdbcDaoSupport {
	@Autowired
	public LOVsDAO(@Qualifier(value = "dataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@PostConstruct
	public void loadAllLovs() {
		loadCurrency();
		loadPOrderStatus();
		loadSite();
		loadPItemStatus();
		loadPTrackingStatus();
		loadCarrier();
		loadSOrderStatus();
		loadDeliveryType();
		loadItemType();
	}
	
	public void loadPItemStatus() {
		String query = "SELECT * FROM PITEM_STATUS";
		List<PitemStatus> pitemStatuses = getJdbcTemplate().query(query, new PItemStatusMapper());
		for (PitemStatus pis : pitemStatuses)
			LOVS.PITEM_STATUS_MAP.put(pis.getKey(), pis);
	}

	private class PItemStatusMapper implements RowMapper<PitemStatus> {
		@Override
		public PitemStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			PitemStatus pitemStatus = new PitemStatus();
			pitemStatus.setKey(rs.getString("KEY"));
			pitemStatus.setDescription(rs.getString("DESCRIPTION"));
			return pitemStatus;
		}
	}
	
	public void loadPTrackingStatus() {
		String query = "SELECT * FROM PTRACKING_STATUS";
		List<PtrackingStatus> ptrackingStatuses = getJdbcTemplate().query(query, new PtrackingStatusMapper());
		for (PtrackingStatus pis : ptrackingStatuses)
			LOVS.PTRACK_STATUS_MAP.put(pis.getKey(), pis);
	}

	private class PtrackingStatusMapper implements RowMapper<PtrackingStatus> {
		@Override
		public PtrackingStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			PtrackingStatus ptrackingStatus = new PtrackingStatus();
			ptrackingStatus.setKey(rs.getString("KEY"));
			ptrackingStatus.setDescription(rs.getString("DESCRIPTION"));
			return ptrackingStatus;
		}
	}
	
	public void loadCarrier() {
		String query = "SELECT * FROM CARRIER";
		List<Carrier> carriers = getJdbcTemplate().query(query, new CarrierMapper());
		for (Carrier carrier : carriers)
			LOVS.CARRIER_MAP.put(carrier.getKey(), carrier);
	}

	private class CarrierMapper implements RowMapper<Carrier> {
		@Override
		public Carrier mapRow(ResultSet rs, int rowNum) throws SQLException {
			Carrier carrier = new Carrier();
			carrier.setKey(rs.getString("KEY"));
			return carrier;
		}
	}

	public void loadPOrderStatus() {
		String query = "SELECT * FROM PORDER_STATUS";
		List<PorderStatus> porderStatuses = getJdbcTemplate().query(query, new POrderStatusMapper());
		for (PorderStatus pos : porderStatuses)
			LOVS.PORDER_STATUS_MAP.put(pos.getKey(), pos);
	}
	
	public void loadSOrderStatus() {
		String query = "SELECT * FROM SORDER_STATUS";
		List<SorderStatus> sorderStatuses = getJdbcTemplate().query(query, new SOrderStatusMapper());
		for (SorderStatus pos : sorderStatuses)
			LOVS.SORDER_STATUS_MAP.put(pos.getKey(), pos);
	}
	

	private class POrderStatusMapper implements RowMapper<PorderStatus> {
		@Override
		public PorderStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			PorderStatus porderStatus = new PorderStatus();
			porderStatus.setKey(rs.getString("KEY"));
			porderStatus.setDescription(rs.getString("DESCRIPTION"));
			return porderStatus;
		}
	}
	
	private class SOrderStatusMapper implements RowMapper<SorderStatus> {
		@Override
		public SorderStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			SorderStatus sorderStatus = new SorderStatus();
			sorderStatus.setKey(rs.getString("KEY"));
			sorderStatus.setDescription(rs.getString("DESCRIPTION"));
			return sorderStatus;
		}
	}
	
	public void loadSite() {
		String query = "SELECT * FROM SITE";
		List<Site> sites = getJdbcTemplate().query(query, new SiteMapper());
		for (Site site : sites)
			LOVS.SITE_MAP.put(site.getKey(), site);
	}

	private class SiteMapper implements RowMapper<Site> {
		@Override
		public Site mapRow(ResultSet rs, int rowNum) throws SQLException {
			Site site = new Site();
			site.setKey(rs.getString("KEY"));
			site.setName(rs.getString("NAME"));
			return site;
		}
	}
	
	public void loadCurrency() {
		String query = "SELECT * FROM CURRENCY";
		List<Currency> currencies = getJdbcTemplate().query(query, new CurrencyMapper());
		for (Currency currency : currencies)
			LOVS.CURRENCY_MAP.put(currency.getKey(), currency);
	}

	private class CurrencyMapper implements RowMapper<Currency> {
		@Override
		public Currency mapRow(ResultSet rs, int rowNum) throws SQLException {
			Currency currency = new Currency();
			currency.setKey(rs.getString("KEY"));
			currency.setToSarRate(rs.getFloat("TO_SAR_RATE"));
			return currency;
		}
	}
	
	public void loadDeliveryType() {
		String query = "SELECT * FROM Delivery_Type";
		List<DeliveryType> deliveryTypes = getJdbcTemplate().query(query, new DeliveryTypeMapper());
		for (DeliveryType pos : deliveryTypes)
			LOVS.DELIVERY_TYPE_MAP.put(pos.getKey(), pos);
	}
	

	private class DeliveryTypeMapper implements RowMapper<DeliveryType> {
		@Override
		public DeliveryType mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryType deliveryType = new DeliveryType();
			deliveryType.setKey(rs.getString("KEY"));
			deliveryType.setDescription(rs.getString("DESCRIPTION"));
			return deliveryType;
		}
	}
	
	public void loadItemType() {
		String query = "SELECT * FROM ITEM_Type";
		List<ItemType> itemTypes = getJdbcTemplate().query(query, new ItemTypeMapper());
		for (ItemType pos : itemTypes)
			LOVS.ITEM_TYPE_MAP.put(pos.getKey(), pos);
	}
	
	private class ItemTypeMapper implements RowMapper<ItemType> {
		@Override
		public ItemType mapRow(ResultSet rs, int rowNum) throws SQLException {
			ItemType itemType = new ItemType();
			itemType.setKey(rs.getString("KEY"));
			return itemType;
		}
	}

}
