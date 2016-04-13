/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.hammam.business.om.jpa.Sorder;

import org.primefaces.push.annotation.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

/**
 * @author Hammam
 *
 */
@Service(value = "sorderDAO")
@Singleton
public class SOrderDAOImp extends JdbcDaoSupport implements SOrederDAO {

	@Autowired
	public SOrderDAOImp(@Qualifier(value = "dataSource") DataSource dataSource) {
		setDataSource(dataSource); 
	}
	
	@Override
	public List<Sorder> searchOrderes(String customerName, String contact, String[] status, int valueFrom, int valueTo,
			Date createdFrom, Date createdTo, String[] deliveryType) {
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid ";
		String where = "";
		
		if(contact != null && !contact.trim().isEmpty())
			where += "so.contact1 LIKE '%"+contact+"%' OR so.contact2 LIKE '%\"+contact+\"%' AND ";
		
		if(customerName != null && !customerName.trim().isEmpty())
			where += "so.customer_name LIKE '%"+customerName+"%' AND ";
		
		if(status != null && status.length>0){
			where += "(";
			for(String s : status){
				where += "so.STATUS = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
//		if(valueFrom > -1)
//			where += "VALUE >= "+valueFrom+" AND ";
//		if(valueTo > -1)
//			where += "VALUE <= "+valueTo+" AND ";
		if(createdFrom != null)
			where += "so.CREATION_DATE >= "+new Timestamp(createdFrom.getTime())+" AND ";
		if(createdTo != null)
			where += "so.CREATION_DATE <= "+new Timestamp(createdTo.getTime())+" AND ";
		
		
		if(!where.isEmpty()){
			where = where.substring(0, where.length()-4);
			query += " and "+ where;
			
		}
		query += " group by s.sorder_seqid";
		System.out.println(query);
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		return sorders;
	}

	@Override
	public List<Sorder> listOrderes() {
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid group by s.sorder_seqid";
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		return sorders;
	}

	@Override
	public List<Sorder> getOrderByCustomerId(String customerName) {
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid and so.CUSTOMER_NAME LIKE \'%"+customerName+"%\' group by s.sorder_seqid";
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		
		return sorders;
	}
	
	@Override
	public List<Sorder> getActiveOrderByCustomerId(String customerName){
		
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid " + 
				" and so.CUSTOMER_NAME LIKE \'%"+customerName+"%\' and ( so.status = \'Under Collection\' or so.status = \'Under Delivery\' )  group by s.sorder_seqid";
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		
		return sorders;
	
	}
	
	@Override
	public Sorder getOrderByExactCustomerName(String customerName) {
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid and so.CUSTOMER_NAME = \'"+customerName+"\' group by s.sorder_seqid";
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		if(sorders!= null && !sorders.isEmpty())
			return sorders.get(0);
		return null;
	}
	
	@Override 
	public Sorder getOrderBySeqId(String Id) {
		String query = "select ifnull(sum(s.discount),0) td, ifnull(sum(s.quantity),0) tq, ifnull(sum(s.quantity * p.SELLING_VALUE),0) tv, so.* from pitem p, sitem s, sorder so where s.pitem_seqid = p.seq_id and so.seqid = s.sorder_seqid and so.SeqID = "+Id+" group by s.sorder_seqid";
		List<Sorder> sorders = getJdbcTemplate().query(query, new SOrderMapper());
		if(sorders!= null && !sorders.isEmpty())
				return sorders.get(0);
		return null;
	}

	private class SOrderMapper implements RowMapper<Sorder> {
		@Override
		public Sorder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			Sorder sorder = new Sorder();
			sorder.setSeqId(rs.getInt("SEQID"));
			sorder.setDeliveryTypeBean(LOVS.DELIVERY_TYPE_MAP.get(rs.getString("DELIVERY_TYPE")));
			sorder.setComment(rs.getString("COMMENT"));
			sorder.setCustomerName(rs.getString("CUSTOMER_NAME"));
			sorder.setCreation_Date(new Date(rs.getTimestamp("CREATION_DATE").getTime()));
			sorder.setContact1(rs.getString("CONTACT1"));
			sorder.setContact2(rs.getString("CONTACT2"));
			sorder.setLocation(rs.getString("LOCATION"));
			sorder.setDeliveryCost(rs.getInt("DELIVERY_COST"));
			sorder.setTotalDiscount(rs.getObject("TD")!=null?rs.getFloat("TD"):0.0f);

			sorder.setTotalValue(rs.getObject("TV")!=null?rs.getFloat("TV"):0.0f);
			sorder.setNoItems(rs.getObject("TQ")!=null?rs.getInt("TQ"):0);
			sorder.setSorderStatus(LOVS.SORDER_STATUS_MAP.get(rs.getString("STATUS")));
	        
			return sorder;
		}
	}

	@Override
	public String insertOrder(Sorder sorder) throws SQLException {
		final String sql = "INSERT INTO sorder " + "(`customer_name`, `contact1`, `contact2`, `location`, `status`, `delivery_type`, `delivery_cost`, `creation_Date`, `comment`)" + "VALUES"
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)";

//		try (Connection connection = getJdbcTemplate().getDataSource().getConnection();
//				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
//
//			statement.setString(1, porder.getSite());
//			statement.setString(2, porder.getOrderId());
//			statement.setFloat(3, porder.getValue().floatValue());
//			statement.setString(4, porder.getCurrencyBean().getKey());
//			statement.setTimestamp(5, porder.getOrderdate());
//			statement.setString(6, porder.getPorderStatus().getKey());
//			statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
//			statement.setString(8, porder.getComment());
//
//			// ...
//
//			int affectedRows = statement.executeUpdate();
//
//			if (affectedRows == 0) {
//				throw new SQLException("Creating user failed, no rows affected.");
//			}
//
//			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//				if (generatedKeys.next()) {
//					porder.setSeqId(generatedKeys.getString(1));
//				} else {
//					throw new SQLException("Creating user failed, no ID obtained.");
//				}
//			}
//		}

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		SimpleJdbcInsert insert;
		String tableName = "SORDER";

		insert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("SEQID");
		final Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("customer_name", sorder.getCustomerName());
		parameters.put("contact1", sorder.getContact1());
		parameters.put("contact2", sorder.getContact2());
		parameters.put("COMMENT", sorder.getComment());
		parameters.put("delivery_cost", sorder.getDeliveryCost());
		parameters.put("delivery_type", sorder.getDeliveryTypeBean().getKey());
		parameters.put("location", sorder.getLocation());
		parameters.put("STATUS", sorder.getSorderStatus().getKey());
		parameters.put("CREATION_DATE", new Timestamp(System.currentTimeMillis()));

		//ResultSet generatedKeys = insert.getJdbcTemplate().getGeneratedKeys();

		KeyHolder key = insert.executeAndReturnKeyHolder(parameters);
		sorder.setSeqId(key.getKey().intValue());
		// key.get
		//
		// getJdbcTemplate().update(new PreparedStatementCreator() {
		//
		// public PreparedStatement createPreparedStatement(Connection
		// connection) throws SQLException {
		//
		// PreparedStatement ps = connection.prepareStatement(sql,
		// Statement.RETURN_GENERATED_KEYS);
		// ps.setString(1, tran.getTransactionType().toString());
		//
		// Date sqlDate = new Date(tran.getDate().getTime());
		// ps.setDate(2, sqlDate);
		// ps.setString(3, tran.getDescription());
		//
		// return ps;
		// }
		// }, keyHolder);

		// int inserts = getJdbcTemplate().update(
		// sql,
		// new Object[] { porder.getSite(), porder.getOrderId(),
		// porder.getValue(), porder.getCurrencyBean().getKey(),
		// porder.getOrderdate(), porder.getPorderStatus().getKey(), new
		// Timestamp(System.currentTimeMillis()), porder.getComment() });
		//
		return "";
	}

	@Override
	public String updateOrder(Sorder sorder) throws Exception {
		// UPDATE `hammambusiness`.`porder`
		// SET
		// `SEQ_ID` = <{SEQ_ID: }>,
		// `SITE` = <{SITE: }>,
		// `ORDER_ID` = <{ORDER_ID: }>,
		// `VALUE` = <{VALUE: }>,
		// `CURRENCY` = <{CURRENCY: }>,
		// `ORDERDATE` = <{ORDERDATE: CURRENT_TIMESTAMP}>,
		// `STATUS` = <{STATUS: }>,
		// `CREATIONDATE` = <{CREATIONDATE: 0000-00-00 00:00:00}>,
		// `COMMENT` = <{COMMENT: }>
		// WHERE `SEQ_ID` = <{expr}>;
		// String updateStmnt = "UPDATE MED_SF_PANORAMA_ROLE_FUNCTIONS SET
		// ACTIVE = ? WHERE FUNCTION_ID = ? AND ROLE_ID = ?";

		// int inserts = 0;
		// jdbcTemplate = new JdbcTemplate(dataSource);
		// dataSource.getConnection().setAutoCommit(false);
		// DefaultTransactionDefinition paramTransactionDefinition = new
		// DefaultTransactionDefinition();
		//
		// TransactionStatus status = transactionManager
		// .getTransaction(paramTransactionDefinition);
		// try {
		// int updates = getJdbcTemplate().update(
		// updateStmnt,
		// new Object[] { panoramaRoleFunction.isActive()?"Y":"N",
		// panoramaRoleFunction.getFunctionId(),
		// panoramaRoleFunction.getRoleId() });
		////
		//// inserts = jdbcTemplate.update(
		//// updateStmnt,
		//// new Object[] { panoramaRoleFunction.isActive()?"Y":"N",
		// panoramaRoleFunction.getFunctionId(),
		// panoramaRoleFunction.getRoleId() });
		////
		//// transactionManager.commit(status);
		// return updates+"";
		//
		// } catch (Exception e) {
		//// transactionManager.rollback(status);
		// throw new Exception(e.getMessage());
		//
		// } finally {
		//
		// }
		String updateStmnt = "UPDATE SORDER SET customer_name = ?, " + 
				"contact1 = ?, " + 
				"contact2 = ?, " + 
				"location = ?, " + 
				"delivery_type = ?, " +
				"delivery_cost = ?, " + 
				"`STATUS` = ?," + 
				"`COMMENT` = ? " + 
				"WHERE SEQID = ?";
//		jdbcTemplate = new JdbcTemplate(dataSource);
//		//dataSource.getConnection().setAutoCommit(true);
//		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
//
//		TransactionStatus status = transactionManager
//				.getTransaction(paramTransactionDefinition);
//		int inserts = 0;
		try {
			int inserts = getJdbcTemplate().update(
					updateStmnt,
					new Object[] { sorder.getCustomerName(), sorder.getContact1(),  sorder.getContact2(), sorder.getLocation(), sorder.getDeliveryTypeBean().getKey(), sorder.getDeliveryCost(), sorder.getSorderStatus().getKey(), sorder.getComment(), sorder.getSeqId() });

//			inserts = jdbcTemplate.update(
//					updateStmnt,
//					new Object[] { panoramaFunction.getAlias(),  panoramaFunction.isActive()?"Y":"N", panoramaFunction.getMenu(), panoramaFunction.getPage(), panoramaFunction.getFunctionId() });
//
//			transactionManager.commit(status);


		} catch (Exception e) {
//			transactionManager.rollback(status);
			throw new Exception(e.getMessage());

		} finally {
			
		}
		return "";
	}

}
