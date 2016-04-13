/**
 * 
 */
package com.hammam.business.om.dao;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.push.annotation.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.hammam.business.om.jpa.Pitem;
import com.hammam.business.om.jpa.Porder;
import com.mysql.jdbc.Blob;

/**
 * @author Hammam
 *
 */
@Service(value = "itemDAO")
@Singleton
public class PItemDAOImp extends JdbcDaoSupport implements PItemDAO {

	@Autowired
	public PItemDAOImp(@Qualifier(value="dataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public String insertItem(Pitem pitem) throws SQLException {
		final String sql = "INSERT INTO PITEM ( `SITE`, `PORDER_ID`, `SITE_ORDER_ID`, `VALUE`, `CURRENCY`," + 
				"`SELLING_VALUE`, MAX_DISCOUNT`, `QUANTITY`, `DELIVERED_QUANTITY`, `URL`, `ORDERDATE`, `STATUS`, `CREATIONDATE`," + 
				"`COMMENT`, `QUANTITY_IN_STOCK`, `NAME`, `PHOTO1`, `TYPE`) VALUES" + 
				"( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		SimpleJdbcInsert insert;
		String tableName = "PITEM";

		insert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("SEQ_ID");
		final Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SITE", pitem.getSite());
		parameters.put("PORDER_ID", pitem.getPorder().getSeqId());
		parameters.put("SITE_ORDER_ID", pitem.getPorder().getOrderId());
		parameters.put("VALUE", pitem.getValue());
		parameters.put("CURRENCY", pitem.getCurrencyBean().getKey());
		parameters.put("SELLING_VALUE", pitem.getSellingValue());
		parameters.put("MAX_DISCOUNT", pitem.getMaxDiscount());
		parameters.put("QUANTITY", pitem.getQuantity());
		parameters.put("DELIVERED_QUANTITY", pitem.getDeliveredQuantity());
		parameters.put("URL", pitem.getUrl());
		parameters.put("ORDERDATE", new Timestamp(pitem.getOrderdate().getTime()));
		parameters.put("STATUS", pitem.getPitemStatus().getKey());
		parameters.put("CREATIONDATE", new Timestamp(System.currentTimeMillis()));
		parameters.put("COMMENT", pitem.getComment());
		parameters.put("QUANTITY_IN_STOCK", pitem.getQuantityInStock());
		parameters.put("NAME", pitem.getName());
		parameters.put("PHOTO1", pitem.getImageBytes());
		parameters.put("TYPE", pitem.getType().getKey());



		KeyHolder key = insert.executeAndReturnKeyHolder(parameters);
		pitem.setSeqId(key.getKey().intValue());
		return "";
	}
	
	@Override
	public byte[] getItemImageById(String id){
		String query = "SELECT PHOTO1 FROM PITEM WHERE SEQ_ID = ? ";
		
		byte[] result = new byte[0];
			try {
				java.sql.Blob imageBlob = getJdbcTemplate().queryForObject(
						query, Blob.class, id);
				result = (imageBlob!= null?imageBlob.getBytes(1, (int) imageBlob.length()):new byte[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}

	@Override
	public String updateItem(Pitem pitem) throws Exception {
		
		String updateStmnt = "UPDATE PITEM SET "+
				"SITE = ?, "+
				"PORDER_ID = ?, "+
				"SITE_ORDER_ID = ?, "+
				"VALUE = ?, "+
				"CURRENCY = ?, "+
				"SELLING_VALUE = ?, "+
				"MAX_DISCOUNT = ?, "+
				"QUANTITY = ?, "+
				"DELIVERED_QUANTITY = ?, "+
				"`URL` = ?, "+
				"ORDERDATE = ?, "+
				"STATUS = ?, "+
				"CREATIONDATE = ?, "+
				"COMMENT = ?, "+
				"QUANTITY_IN_STOCK = ?, "+
				"`NAME` = ?, "+
				"`TYPE` = ?, "+
				"PHOTO1 = ? "+
				"WHERE SEQ_ID = ? ";
				JdbcTemplate jt;
		try {
			jt = getJdbcTemplate();
			int inserts = jt.update(
					updateStmnt,
					new Object[] { pitem.getSite(), pitem.getPorder().getSeqId(), pitem.getPorder().getOrderId(), pitem.getValue(), pitem.getCurrencyBean().getKey(),
							pitem.getSellingValue(), pitem.getMaxDiscount(), pitem.getQuantity(), pitem.getDeliveredQuantity(), pitem.getUrl(),
							pitem.getPorder().getOrderdate(), pitem.getPitemStatus().getKey(), pitem.getCreationdate(),
							pitem.getComment(), pitem.getQuantityInStock(), pitem.getName(),pitem.getType().getKey(), pitem.getImageBytes(), pitem.getSeqId() });

		} catch (Exception e) {
			throw new Exception(e.getMessage());

		} finally {
			
		}
		return "";

	}
	

	private class PItemMapper implements RowMapper<Pitem> {
		@Override
		public Pitem mapRow(ResultSet rs, int rowNum) throws SQLException {
			Pitem pitem = new Pitem();

			pitem.setSeqId(rs.getInt("SEQ_ID"));
			pitem.setSite(rs.getString("SITE"));
			pitem.setComment(rs.getString("COMMENT"));
			pitem.setValue(rs.getBigDecimal("VALUE"));
			pitem.setSellingValue(rs.getBigDecimal("SELLING_VALUE"));
			pitem.setMaxDiscount(rs.getBigDecimal("MAX_DISCOUNT"));
			pitem.setQuantity(rs.getInt("QUANTITY"));
			pitem.setDeliveredQuantity(rs.getInt("DELIVERED_QUANTITY"));
			pitem.setQuantityInStock(rs.getInt("QUANTITY_IN_STOCK"));
			pitem.setCreationdate(new Date(rs.getTimestamp("CREATIONDATE").getTime()));
			pitem.setOrderdate(new Date(rs.getTimestamp("ORDERDATE").getTime()));
			pitem.setCurrencyBean(LOVS.CURRENCY_MAP.get(rs.getString("CURRENCY")));
			pitem.setPitemStatus(LOVS.PITEM_STATUS_MAP.get(rs.getString("STATUS")));
			pitem.setName(rs.getString("NAME"));
			pitem.setUrl(rs.getString("URL"));
			pitem.setSiteOrderId(rs.getString("SITE_ORDER_ID"));
			pitem.setType(LOVS.ITEM_TYPE_MAP.get(rs.getString("type")));
			java.sql.Blob imageBlob = rs.getBlob("PHOTO1");
			pitem.setImageBytes(imageBlob!= null?imageBlob.getBytes(1, (int) imageBlob.length()):new byte[0]);
			Porder po = new Porder();
			po.setSeqId(rs.getInt("PORDER_ID"));
			po.setOrderId(rs.getString("SITE_ORDER_ID"));
			pitem.setPorder(po);
			po.getItemsPhotosMap().put(pitem.getSeqId()+"", pitem.getImageBytes());
			return pitem;
		}
	}
	
	@Override
	public List<Pitem> listItemsByType(String[] itemTypes) {
		String where ="";
		if(itemTypes != null && itemTypes.length>0){
			where += "(";
			for(String s : itemTypes){
				where += "TYPE = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		if(!where.isEmpty())
			where = " WHERE "+where.substring(0, where.length()-4);
		
		String query = "SELECT * FROM PITEM "+where;
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}
	
	@Override
	public List<Pitem> listAvailableItemsByType(String[] itemTypes) {
		String where ="";
		if(itemTypes != null && itemTypes.length>0){
			where += "(";
			for(String s : itemTypes){
				where += "TYPE = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		if(!where.isEmpty())
			where = " AND "+where.substring(0, where.length()-4);
		
		String query = "SELECT * FROM PITEM Where QUANTITY_IN_STOCK > 0 "+where;
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}

	@Override
	public List<Pitem> listItems() {
		String query = "SELECT * FROM PITEM";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}
	
	@Override
	public List<Pitem> listAvailableItems() {
		String query = "SELECT * FROM PITEM Where QUANTITY_IN_STOCK > 0";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}

	@Override
	public Pitem getItemById(String Id) {
		String query = "SELECT * FROM PITEM WHERE SEQ_ID = "+Id+" ";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		if(pitems!= null && !pitems.isEmpty())
				return pitems.get(0);
		return null;
	}

	@Override
	public List<Pitem> getItemByName(String name) {
		String query = "SELECT * FROM PITEM WHERE NAME LIKE \'%"+name+"%\' ";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}

	@Override
	public List<Pitem> listItemsOfOrder(String orderId) {
		String query = "SELECT * FROM PITEM WHERE SITE_ORDER_ID LIKE \'%"+orderId+"%\' ";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}
	
	@Override
	public List<Pitem> listItemsOfExactOrder(String orderId) {
		String query = "SELECT * FROM PITEM WHERE SITE_ORDER_ID = \'"+orderId+"\' ";
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}

	@Override
	public List<Pitem> searchItems(String[] site, String orderId, String[] status, String[] itemTypes, int valueFrom, int valueTo,
			int svalueFrom, int svalueTo, String name, Date createdFrom, Date createdTo,
			Date orderedFrom, Date orderedTo, int quantityFrom, int quantityTo, int stockFrom, int stockTo) {
		String query = "SELECT * FROM PITEM ";
		String where = "";
		if(site != null && site.length>0){
			where += "(";
			for(String s : site){
				where += "SITE = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		
		if(itemTypes != null && itemTypes.length>0){
			where += "(";
			for(String s : itemTypes){
				where += "TYPE = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		if(orderId != null && !orderId.trim().isEmpty())
			where += "SITE_ORDER_ID LIKE '%"+orderId+"%' AND ";
		if(status != null && status.length>0){
			where += "(";
			for(String s : status){
				where += "STATUS = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		if(valueFrom > -1)
			where += "VALUE >= "+valueFrom+" AND ";
		if(valueTo > -1)
			where += "VALUE <= "+valueTo+" AND ";
		if(svalueFrom > -1)
			where += "SELLING_VALUE >= "+svalueFrom+" AND ";
		if(svalueTo > -1)
			where += "SELLING_VALUE <= "+svalueTo+" AND ";
		
		if(createdFrom != null)
			where += "CREATIONDATE >= "+new Timestamp(createdFrom.getTime())+" AND ";
		if(createdTo != null)
			where += "CREATIONDATE <= "+new Timestamp(createdTo.getTime())+" AND ";
		if(orderedFrom != null)
			where += "ORDERDATE >= "+new Timestamp(orderedFrom.getTime())+" AND ";
		if(orderedTo != null)
			where += "ORDERDATE <= "+new Timestamp(orderedTo.getTime())+" AND ";
		
		if(name != null)
			where += "NAME LIKE \'%"+name+"%\' AND ";
		
		
		if(quantityFrom > -1)
			where += "DELIVERED_QUANTITY >= "+quantityFrom+" AND ";
		if(quantityTo > -1)
			where += "DELIVERED_QUANTITY <= "+quantityTo+" AND ";
		if(stockFrom > -1)
			where += "QUANTITY_IN_STOCK >= "+stockFrom+" AND ";
		if(stockTo > -1)
			where += "QUANTITY_IN_STOCK <= "+stockTo+" AND ";
		
		where = where.substring(0, where.length()-4);
		if(!where.isEmpty())query += " WHERE "+ where;
		List<Pitem> pitems = getJdbcTemplate().query(query, new PItemMapper());
		return pitems;
	}
	
	
}
