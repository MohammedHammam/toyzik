/**
 * 
 */
package com.hammam.business.om.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.hammam.business.om.jpa.Pitem;

/**
 * @author Hammam
 *
 */
@Service
@Lazy
public class PTrackingDAOImp extends JdbcDaoSupport implements PTrackingDAO {

	@Autowired
	public PTrackingDAOImp(@Qualifier(value="dataSource") DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.hammam.business.om.dao.PItemDAO#saveItem(com.hammam.business.om.jpa.Pitem)
	 */
	@Override
	public String saveItem(Pitem pitem) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hammam.business.om.dao.PItemDAO#listItems()
	 */
	@Override
	public List<Pitem> listItems() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hammam.business.om.dao.PItemDAO#getItemById(java.lang.String)
	 */
	@Override
	public Pitem getItemById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

}
