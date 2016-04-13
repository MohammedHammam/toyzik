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
import org.springframework.transaction.annotation.Transactional;

import com.hammam.business.om.jpa.Sitem;
import com.hammam.business.om.jpa.Sorder;
import com.hammam.business.om.jpa.Porder;
import com.hammam.business.om.jpa.Sitem;
import com.mysql.jdbc.Blob;

/**
 * @author Hammam
 *
 */
@Service(value = "sitemDAO")
@Singleton
public class SItemDAOImp extends JdbcDaoSupport implements SItemDAO {

	@Autowired
	public SItemDAOImp(@Qualifier(value="dataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public String insertItem(Sitem sitem) throws SQLException {
		final String sql = "INSERT INTO SITEM ( `sorder_seqId`, `pitem_seqId`, `quantity`, `discount`, `comment`) VALUES" + 
				"( ?, ?, ?, ?, ? );";

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		SimpleJdbcInsert insert;
		String tableName = "SITEM";

		insert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("SEQID");
		final Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("sorder_seqId", sitem.getSorder().getSeqId());
		parameters.put("pitem_seqId", sitem.getPitem_seqId());
		parameters.put("quantity", sitem.getQuantity());
		parameters.put("discount", sitem.getDiscount());
		parameters.put("comment", sitem.getComment());



		KeyHolder key = insert.executeAndReturnKeyHolder(parameters);
		sitem.setSeqId(key.getKey().intValue());
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
	public String updateItem(Sitem sitem) throws Exception {
		
		String updateStmnt = "UPDATE SITEM SET "+
				"quantity = ?, "+
				"discount = ?, "+
				"comment = ? "+
				"WHERE SEQID = ? ";
				JdbcTemplate jt;
		try {
			jt = getJdbcTemplate();
			int inserts = jt.update(
					updateStmnt,
					new Object[] { sitem.getQuantity(), sitem.getDiscount(), sitem.getComment(), sitem.getSeqId() });

		} catch (Exception e) {
			throw new Exception(e.getMessage());

		} finally {
			
		}
		return "";

	}
	

	private class PItemMapper implements RowMapper<Sitem> {
		@Override
		public Sitem mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sitem sitem = new Sitem();

			sitem.setSeqId(rs.getInt("SEQID"));
			sitem.setQuantity(rs.getInt("quantity"));
			sitem.setComment(rs.getString("COMMENT"));
			sitem.setDiscount(rs.getFloat("discount"));
			
			sitem.setPitem_seqId(rs.getInt("pitem_seqId"));
			Sorder po = new Sorder();
			po.setSeqId(rs.getInt("sorder_seqId"));
			sitem.setSorder(po);
//			sitem.setQuantity(rs.getInt("QUANTITY"));
//			sitem.setDeliveredQuantity(rs.getInt("DELIVERED_QUANTITY"));
//			sitem.setQuantityInStock(rs.getInt("QUANTITY_IN_STOCK"));
//			sitem.setCreationdate(new Date(rs.getTimestamp("CREATIONDATE").getTime()));
//			sitem.setOrderdate(new Date(rs.getTimestamp("ORDERDATE").getTime()));
//			sitem.setCurrencyBean(LOVS.CURRENCY_MAP.get(rs.getString("CURRENCY")));
//			sitem.setSitemStatus(LOVS.PITEM_STATUS_MAP.get(rs.getString("STATUS")));
//			sitem.setName(rs.getString("NAME"));
//			sitem.setUrl(rs.getString("URL"));
//			sitem.setSiteOrderId(rs.getString("SITE_ORDER_ID"));
//			java.sql.Blob imageBlob = rs.getBlob("PHOTO1");
//			sitem.setImageBytes(imageBlob!= null?imageBlob.getBytes(1, (int) imageBlob.length()):new byte[0]);
//			Porder po = new Porder();
//			po.setSeqId(rs.getInt("PORDER_ID"));
//			po.setOrderId(rs.getString("SITE_ORDER_ID"));
//			sitem.setPorder(po);
//			po.getItemsPhotosMap().put(sitem.getSeqId()+"", sitem.getImageBytes());
			return sitem;
		}
	}

	@Override
	public List<Sitem> listItems() {
		String query = "SELECT * FROM SITEM";
		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
		return sitems;
	}
	
	@Transactional
	public void addItemToOrder(Sorder so, int q, String seqId, int sq) throws Exception{
		String query = "SELECT * FROM SITEM WHERE sorder_seqId = "+so.getSeqId()+" and pitem_seqId = "+seqId;
		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
		if(sitems !=null && !sitems.isEmpty()){
			Sitem theItem = sitems.get(0);
			theItem.setQuantity(theItem.getQuantity()+q);
			updateItem(theItem);
//			String updateStmnt = "UPDATE PITEM SET "+
//					"QUANTITY_IN_STOCK = ? "+
//					"WHERE SEQ_ID = ? ";
//			JdbcTemplate jt = getJdbcTemplate();
//			int inserts = jt.update(
//					updateStmnt,
//					new Object[] { sq, seqId});

		}else{
			Sitem theItem = new Sitem();
			theItem.setQuantity(q);
			theItem.setSorder(so);
			theItem.setPitem_seqId(Integer.parseInt(seqId));
			insertItem(theItem);
//			String updateStmnt = "UPDATE PITEM SET "+
//					"QUANTITY_IN_STOCK = ? "+
//					"WHERE SEQ_ID = ? ";
//			JdbcTemplate jt = getJdbcTemplate();
//			int inserts = jt.update(
//					updateStmnt,
//					new Object[] { sq, seqId});
		}
		
		
	}
	
	public void removeItemFromOrder(Sitem item) throws Exception{
		String updateStmnt = "DELETE FROM SITEM "+
				"WHERE SEQID = ? ";
				JdbcTemplate jt;
		try {
			jt = getJdbcTemplate();
			int inserts = jt.update(
					updateStmnt,
					new Object[] { item.getSeqId() });

		} catch (Exception e) {
			throw new Exception(e.getMessage());

		} finally {
			
		}

	}
	public void addItemToOrder(Sitem item) throws Exception{
			updateItem(item);
	}
//	@Override
//	public Sitem getItemById(String Id) {
//		String query = "SELECT * FROM PITEM WHERE SEQ_ID = "+Id+" ";
//		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
//		if(sitems!= null && !sitems.isEmpty())
//				return sitems.get(0);
//		return null;
//	}
//
//	@Override
//	public List<Sitem> getItemByName(String name) {
//		String query = "SELECT * FROM PITEM WHERE NAME LIKE \'%"+name+"%\' ";
//		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
//		return sitems;
//	}
//
	@Override
	public List<Sitem> listItemsOfOrder(int orderId) {
		String query = "SELECT * FROM SITEM WHERE sorder_seqId = "+orderId+" ";
		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
		return sitems;
	}
//	
//	@Override
//	public List<Sitem> listItemsOfExactOrder(String orderId) {
//		String query = "SELECT * FROM PITEM WHERE SITE_ORDER_ID = \'"+orderId+"\' ";
//		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
//		return sitems;
//	}
//
//	@Override
//	public List<Sitem> searchItems(String[] site, String orderId, String[] status, int valueFrom, int valueTo,
//			int svalueFrom, int svalueTo, String name, Date createdFrom, Date createdTo,
//			Date orderedFrom, Date orderedTo, int quantityFrom, int quantityTo, int stockFrom, int stockTo) {
//		String query = "SELECT * FROM PITEM ";
//		String where = "";
//		if(site != null && site.length>0){
//			where += "(";
//			for(String s : site){
//				where += "SITE = '"+s+"' OR ";
//			}
//			where = where.substring(0, where.length()-3);
//			where += ") AND ";
//		}
//		if(orderId != null && !orderId.trim().isEmpty())
//			where += "SITE_ORDER_ID LIKE '%"+orderId+"%' AND ";
//		if(status != null && status.length>0){
//			where += "(";
//			for(String s : status){
//				where += "STATUS = '"+s+"' OR ";
//			}
//			where = where.substring(0, where.length()-3);
//			where += ") AND ";
//		}
//		if(valueFrom > -1)
//			where += "VALUE >= "+valueFrom+" AND ";
//		if(valueTo > -1)
//			where += "VALUE <= "+valueTo+" AND ";
//		if(svalueFrom > -1)
//			where += "SELLING_VALUE >= "+svalueFrom+" AND ";
//		if(svalueTo > -1)
//			where += "SELLING_VALUE <= "+svalueTo+" AND ";
//		
//		if(createdFrom != null)
//			where += "CREATIONDATE >= "+new Timestamp(createdFrom.getTime())+" AND ";
//		if(createdTo != null)
//			where += "CREATIONDATE <= "+new Timestamp(createdTo.getTime())+" AND ";
//		if(orderedFrom != null)
//			where += "ORDERDATE >= "+new Timestamp(orderedFrom.getTime())+" AND ";
//		if(orderedTo != null)
//			where += "ORDERDATE <= "+new Timestamp(orderedTo.getTime())+" AND ";
//		
//		if(name != null)
//			where += "NAME LIKE \'%"+name+"%\' AND ";
//		
//		
//		if(quantityFrom > -1)
//			where += "DELIVERED_QUANTITY >= "+quantityFrom+" AND ";
//		if(quantityTo > -1)
//			where += "DELIVERED_QUANTITY <= "+quantityTo+" AND ";
//		if(stockFrom > -1)
//			where += "QUANTITY_IN_STOCK >= "+stockFrom+" AND ";
//		if(stockTo > -1)
//			where += "QUANTITY_IN_STOCK <= "+stockTo+" AND ";
//		
//		where = where.substring(0, where.length()-4);
//		if(!where.isEmpty())query += " WHERE "+ where;
//		List<Sitem> sitems = getJdbcTemplate().query(query, new PItemMapper());
//		return sitems;
//	}
//	
	
}
