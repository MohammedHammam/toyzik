/**
 * 
 */
package com.hammam.business.om.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.hammam.business.om.jpa.Porder;

import org.primefaces.push.annotation.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
@Service(value = "orderDAO")
@Singleton
public class POrderDAOImp extends JdbcDaoSupport implements POrederDAO {

	@Autowired
	public POrderDAOImp(@Qualifier(value = "dataSource") DataSource dataSource) {
		setDataSource(dataSource); 
	}
	
	@Override
	public List<Porder> searchOrderes(String[] site, String orderId, String[] status, int valueFrom, int valueTo, Date createdFrom, Date createdTo, Date orderedFrom, Date orderedTo) {
		String query = "SELECT * FROM PORDER ";
		String where = "";
		if(site != null && site.length>0){
			where += "(";
			for(String s : site){
				where += "SITE = '"+s+"' OR ";
			}
			where = where.substring(0, where.length()-3);
			where += ") AND ";
		}
		if(orderId != null && !orderId.trim().isEmpty())
			where += "ORDER_ID LIKE '%"+orderId+"%' AND ";
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
		if(createdFrom != null)
			where += "CREATIONDATE >= "+new Timestamp(createdFrom.getTime())+" AND ";
		if(createdTo != null)
			where += "CREATIONDATE <= "+new Timestamp(createdTo.getTime())+" AND ";
		if(orderedFrom != null)
			where += "ORDERDATE >= "+new Timestamp(orderedFrom.getTime())+" AND ";
		if(orderedTo != null)
			where += "ORDERDATE <= "+new Timestamp(orderedTo.getTime())+" AND ";
		
		
		if(!where.isEmpty()){
			where = where.substring(0, where.length()-4);
			query += " WHERE "+ where;
		}
		List<Porder> porders = getJdbcTemplate().query(query, new POrderMapper());
		return porders;
	}

	@Override
	public List<Porder> listOrderes() {
		String query = "SELECT * FROM PORDER";
		List<Porder> porders = getJdbcTemplate().query(query, new POrderMapper());
		return porders;
	}

	@Override
	public Porder getOrderById(String Id) {
		String query = "SELECT * FROM PORDER WHERE ORDER_ID = \'"+Id+"\'";
		List<Porder> porders = getJdbcTemplate().query(query, new POrderMapper());
		if(porders!= null && !porders.isEmpty())
				return porders.get(0);
		return null;
	}

	private class POrderMapper implements RowMapper<Porder> {
		@Override
		public Porder mapRow(ResultSet rs, int rowNum) throws SQLException {
			Porder porder = new Porder();
			porder.setSeqId(rs.getInt("SEQ_ID"));
			porder.setSite(LOVS.SITE_MAP.get(rs.getString("SITE")));
	        porder.setComment(rs.getString("COMMENT"));
	        porder.setOrderId(rs.getString("ORDER_ID"));
	        porder.setValue(rs.getBigDecimal("VALUE"));
	        porder.setCreationdate(new Date(rs.getTimestamp("CREATIONDATE").getTime()));
	        porder.setOrderdate(new Date(rs.getTimestamp("ORDERDATE").getTime()));
	        porder.setCurrencyBean(LOVS.CURRENCY_MAP.get(rs.getString("CURRENCY")));
	        porder.setPorderStatus(LOVS.PORDER_STATUS_MAP.get(rs.getString("STATUS")));
	        porder.setEFC(rs.getFloat("EFC"));
	        
			return porder;
		}
	}

	@Override
	public String insertOrder(Porder porder) throws SQLException {
		final String sql = "INSERT INTO porder " + "(`SITE`," + "`ORDER_ID`," + "`VALUE`," + "`CURRENCY`,"
				+ "`ORDERDATE`," + "`STATUS`," + "`CREATIONDATE`," + "`COMMENT`, EFC)" + "VALUES"
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
		String tableName = "PORDER";

		insert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("SEQ_ID");
		final Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SITE", porder.getSite().getKey());
		parameters.put("ORDER_ID", porder.getOrderId());
		parameters.put("VALUE", porder.getValue());
		parameters.put("CURRENCY", porder.getCurrencyBean().getKey());
		parameters.put("ORDERDATE", porder.getOrderdate());
		parameters.put("STATUS", porder.getPorderStatus().getKey());
		parameters.put("CREATIONDATE", new Timestamp(System.currentTimeMillis()));
		parameters.put("COMMENT", porder.getComment());
		parameters.put("EFC", porder.getEFC());

		//ResultSet generatedKeys = insert.getJdbcTemplate().getGeneratedKeys();

		KeyHolder key = insert.executeAndReturnKeyHolder(parameters);
		porder.setSeqId(key.getKey().intValue());
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
	public String updateOrder(Porder porder) throws Exception {
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
		String updateStmnt = "UPDATE PORDER SET SITE = ?," + 
				"ORDER_ID = ?," + 
				"VALUE = ?," + 
				"CURRENCY = ?," + 
				"ORDERDATE = ?," + 
				"STATUS = ?," + 
				"COMMENT = ?, " + 
				"EFC = ? "+
				"WHERE SEQ_ID = ?";
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
					new Object[] { porder.getSite().getKey(), porder.getOrderId(),  porder.getValue(), porder.getCurrencyBean().getKey(), porder.getOrderdate(), porder.getPorderStatus().getKey(), porder.getComment(), porder.getEFC(), porder.getSeqId() });

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
