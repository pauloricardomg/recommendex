package com.paulormg.recommendex.server.dao.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;

import com.paulormg.recommendex.server.dao.user.UserDAO;
import com.paulormg.recommendex.server.exception.DBException;
import com.paulormg.recommendex.server.util.DBUtils;
import com.paulormg.recommendex.shared.transfer.Item;

/*
 * Ideally all DAOS should be interfaces instantianted and
 * fetched by dependency injection, but let's keep it simple
 * for now.
 */
public class ItemDAO {

	/* STATIC STUFF */
	
	public static final String[] images = {"http://upload.wikimedia.org/wikipedia/commons/2/24/Amalthea_-_book_cover.jpg", 
										   "http://upload.wikimedia.org/wikipedia/commons/b/b9/Yellow_book_cover.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/9/9d/Royal_book_cover.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/2/21/Book_Cover_Sonnenfinsternis.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/a/a7/Kurzfilmwettbewerb.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/2/2f/Oleolson1.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/6/6d/Poster_Location_Africa.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/4/40/Post-It-Awards_2008_yaroslav-subbotin.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/a/a3/PP_D079_poster_by_grasset_for_l%27encre_marquet.jpg",
										   "http://upload.wikimedia.org/wikipedia/commons/b/b8/So_Savoeun_2.jpg"};
	
	private static Random rand = new Random();
	
	private static String getRandomItemImg() {
		return images[rand.nextInt(images.length)];
	}	
	
	private static final String SQL_GET_ALL_ITEMS = "SELECT * FROM Item";
	private static final String SQL_GET_SOME_ITEMS = "SELECT * FROM Item WHERE id IN (%s)";	
	private static final String SQL_CREATE_ITEM = "INSERT INTO Item VALUES (default, ?, ?, ?)";
	private static final String SQL_REMOVE_ITEM = "DELETE FROM Item WHERE id = ?";	
	
	private static final String SQL_CREATE_TRANSACTION = "INSERT INTO Transaction VALUES (default, ?, default)";
	private static final String SQL_ADD_ITEM_TRANSACTION = "INSERT INTO Transaction_Item VALUES (?, ?, ?, ?)";
	
	
	static Logger logger = Logger.getLogger(UserDAO.class);	
	
	// Singleton
	
	private static ItemDAO instance = null;

	public static ItemDAO getInstance(){
		if (instance == null){
			instance = new ItemDAO();
		}
		
		return instance;
	}
	
	private ItemDAO(){
		// Private constructor
	}
	
	/* ITEM CRUD */	
	
	public Map<Integer, Item> getAllItems() throws DBException{

		Connection conn = DBUtils.getConnection();
		
		ResultSet result = null;
		try {
			Statement stmt = conn.createStatement();
			result = stmt.executeQuery(SQL_GET_ALL_ITEMS);
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.error("Error while executing DB query to retrieve all items: " + e.getMessage());
			throw new DBException();
		}
		
		HashMap<Integer, Item> items = new HashMap<Integer, Item>();
		
		try {
			while(result.next()){
				Item item = getItem(result);
				items.put(item.getId(), item);
			}
			result.close();
		} catch (SQLException e) {
			logger.error("Error while retrieving all items from DB result: " + e.getMessage());
			throw new DBException();
		}
		
		logger.debug("Retrieved all items from DB.");
		
		return items;
	}
	
	public Map<Integer, Item> getItems(Integer[] ids) throws DBException{
		
		Connection conn = DBUtils.getConnection();
		
		ResultSet result = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(prepareGetItemsStatement(ids.length));
			for (int i = 1; i <= ids.length; i++) {
				stmt.setInt(i, ids[i-1]);
			}
			result = stmt.executeQuery();
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error while executing DB query to retrieve some items: " + e.getMessage());
			throw new DBException();
		}
		
		HashMap<Integer, Item> items = new HashMap<Integer, Item>();
		
		try {
			while(result.next()){
				Item item = getItem(result);
				items.put(item.getId(), item);
			}
			result.close();
		} catch (SQLException e) {
			logger.error("Error while retrieving some items from DB result: " + e.getMessage());
			throw new DBException();
		}
		
		logger.debug(String.format("Retrieved %d items from DB.", items.size()));	
		
		return items;
	}

	public boolean removeItem(int itemId) throws DBException{
		Connection conn = DBUtils.getConnection();
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_REMOVE_ITEM);
			stmt.setInt(1, itemId);	
		} catch (SQLException e) {
			logger.error("Error while creating DB query to remove item: " + e.getMessage());
			throw new DBException();
		}
				
		try {
			int rowCount = stmt.executeUpdate();
			stmt.close();
			logger.debug(String.format("Removed item %d from DB.", itemId));			
			return rowCount == 1;
		} catch (SQLException e) {
			logger.error("Error while executing DB query to remove item: " + e.getMessage());
			throw new DBException();
		}
		
	}

	public int addItem(Item item) throws DBException {
		Connection conn = DBUtils.getConnection();
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_CREATE_ITEM, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, item.getName());
			stmt.setString(2, item.getDesc());
			stmt.setString(3, getRandomItemImg());			
		} catch (SQLException e) {
			logger.error("Error while creating DB query to create user: " + e.getMessage());
			throw new DBException();
		}
		
		int itemId = -1;
		
		try {
			stmt.executeUpdate();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (!generatedKeys.next()){
				throw new DBException("Should have generated a new id for item.");			
			}
			itemId = generatedKeys.getInt(1);
			stmt.close();
		} catch (SQLException e) {
			logger.error("Error while executing DB query to create user: " + e.getMessage());
			throw new DBException();
		}
		
		logger.debug(String.format("Item %d succesfully added to DB.", itemId));		
		
		return itemId;
	}
	
	/* PURCHASES */

	public void checkout(int userId, Map<Integer, Integer> itemsCart) throws DBException {
		Connection conn = DBUtils.getConnection();
		
		int transactionId = createCheckoutTransaction(conn, userId);
		addItemsToTransaction(conn, transactionId, userId, itemsCart);
		
		logger.debug(String.format("Checkout transaction of user %d succesfully processed on DB with %d items.", 
									userId, itemsCart.size()));
	}
	
	/* HELPER METHODS */
	
	
	private String prepareGetItemsStatement(int length) {
	    StringBuilder builder = new StringBuilder(length * 2 - 1);
	    for (int i = 0; i < length; i++) {
	        if (i > 0) builder.append(',');
	        builder.append('?');
	    }
	    String placeHolder = builder.toString();
		
		return String.format(SQL_GET_SOME_ITEMS, placeHolder);
	}	
	
	private void addItemsToTransaction(Connection conn, int transactionId, int userId, Map<Integer, Integer> itemsCart) throws DBException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_ADD_ITEM_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
			for (Entry<Integer, Integer> itemQuantity : itemsCart.entrySet()) {
				stmt.setInt(1, transactionId);
				stmt.setInt(2, userId);
				stmt.setInt(3, itemQuantity.getKey());
				stmt.setInt(4, itemQuantity.getValue());
				stmt.addBatch();
			}
		} catch (SQLException e) {
			logger.error("Error while creating DB query to add items to transaction: " + e.getMessage());
			throw new DBException();
		}
				
		try {
			stmt.executeBatch();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Error while executing DB query to create checkout transaction: " + e.getMessage());
			throw new DBException();
		}
		
	}

	private int createCheckoutTransaction(Connection conn, int userId) throws DBException {
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_CREATE_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userId);
		} catch (SQLException e) {
			logger.error("Error while creating DB query to add items to transactions: " + e.getMessage());
			throw new DBException();
		}
		
		int transactionId = -1;
		
		try {
			stmt.executeUpdate();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (!generatedKeys.next()){
				throw new DBException("Should have generated a new id for transaction.");			
			}
			transactionId = generatedKeys.getInt(1);
			generatedKeys.close();
		} catch (SQLException e) {
			logger.error("Error while executing DB query to create checkout transaction: " + e.getMessage());
			throw new DBException();
		}
		
		return transactionId;
	}

	private Item getItem(ResultSet result) throws SQLException {
		Item item = new Item();
		item.setId(result.getInt(1));
		item.setName(result.getString(2));
		item.setDesc(result.getString(3));
		item.setImgUrl(result.getString(4));
		return item;
	}	
}
