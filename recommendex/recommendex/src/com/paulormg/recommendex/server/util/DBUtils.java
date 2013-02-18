package com.paulormg.recommendex.server.util;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.paulormg.recommendex.server.exception.DBException;

public class DBUtils {

	static Logger logger = Logger.getLogger(DBUtils.class);	

	private static DataSource ds = null;

	static {
		init();
	}

	public static void init() {

		try {
			/*
			 * Create a JNDI Initial context to be able to
			 *  lookup  the DataSource
			 *
			 * In production-level code, this should be cached as
			 * an instance or static variable, as it can
			 * be quite expensive to create a JNDI context.
			 *
			 * Note: This code only works when you are using servlets
			 * or EJBs in a J2EE application server. If you are
			 * using connection pooling in standalone Java code, you
			 * will have to create/configure datasources using whatever
			 * mechanisms your particular connection pooling library
			 * provides.
			 */

			InitialContext ctx = new InitialContext();

			/*
			 * Lookup the DataSource, which will be backed by a pool
			 * that the application server provides. DataSource instances
			 * are also a good candidate for caching as an instance
			 * variable, as JNDI lookups can be expensive as well.
			 */

			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/MySQLDB");		
		} catch(NamingException exc){
			exc.printStackTrace();
			logger.error(String.format("Error while getting Data Source" +
					" from JNDI initial context: %s",
					exc.getMessage()));
		}	
	}	
	
	public static Connection getConnection() throws DBException {
		if (ds == null){
			throw new DBException("Could not get DB connection.");
		}		
		
		
		try {
			return ds.getConnection();
		} catch (Exception e) {
			logger.error("Error while opening connection with DB: " + e.getMessage());
			throw new DBException();
		}
	}		

}
