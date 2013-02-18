package com.paulormg.recommendex.server.services.item;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paulormg.recommendex.client.services.item.ItemService;
import com.paulormg.recommendex.server.dao.item.ItemDAO;
import com.paulormg.recommendex.server.dao.stats.StatisticsDAO;
import com.paulormg.recommendex.server.exception.DBException;
import com.paulormg.recommendex.server.services.stats.StatisticsServiceImpl;
import com.paulormg.recommendex.server.util.ServerUtils;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.exception.ServerError;
import com.paulormg.recommendex.shared.transfer.Item;
import com.paulormg.recommendex.shared.transfer.User;

@SuppressWarnings("serial")
public class ItemServiceImpl extends RemoteServiceServlet implements ItemService {

	private static final int MAX_RELATED = 3;
	private ItemDAO itemDao = ItemDAO.getInstance();
	private StatisticsDAO statsDao = StatisticsDAO.getInstance();
	
	static Logger logger = Logger.getLogger(StatisticsServiceImpl.class);	
	
	@Override
	public void addItem(String login, Item item)
			throws InvalidCredentialsException, ServerError {
		
		User user = ServerUtils.authenticate(this.getThreadLocalRequest(), login);
		if (!user.isAdmin()){
			throw new InvalidCredentialsException("User does not have admin rights.");
		}
				
		int id = -1;
		try {
			id = itemDao.addItem(item);
		} catch (DBException e) {
			throw new ServerError();
		}
		logger.info(String.format("New item was added with id: %d.", id));		
	}

	@Override
	public boolean removeItem(String login, int itemId)
			throws InvalidCredentialsException, ServerError {

		User user = ServerUtils.authenticate(this.getThreadLocalRequest(), login);
		if (!user.isAdmin()){
			throw new InvalidCredentialsException("User does not have admin rights");
		}
		
		logger.info(String.format("Item with id %d will be removed.", itemId));
		
		try {
			return itemDao.removeItem(itemId);
		} catch (DBException e) {
			throw new ServerError();
		}
	}

	@Override
	public Map<Integer, Item> getItems(String login) throws InvalidCredentialsException, ServerError {

		ServerUtils.authenticate(this.getThreadLocalRequest(), login);
		
		try {
			return itemDao.getAllItems();
		} catch (DBException e) {
			throw new ServerError();
		}
	}
	
	@Override
	public Map<Integer, Item> getRelatedItems(String login, int itemId) throws InvalidCredentialsException, ServerError {

		ServerUtils.authenticate(this.getThreadLocalRequest(), login);
		
		try {
			return itemDao.getItems(statsDao.getRelatedItems(itemId, MAX_RELATED));
		} catch (DBException e) {
			throw new ServerError();
		}
	}	
	
	@Override
	public boolean checkout(String login, Map<Integer, Integer> itemsCart) throws InvalidCredentialsException, ServerError {
		
		User user = ServerUtils.authenticate(this.getThreadLocalRequest(), login);

		HttpSession session = getThreadLocalRequest().getSession(false);		
		
		logger.info(String.format("User %d in session %s has bought items: %s.", 
				user.getId(), session.getId(), itemsCart));
		
		try {
			itemDao.checkout(user.getId(), itemsCart);
		} catch (DBException e) {
			throw new ServerError();
		}
		
		statsDao.boughtItems(user.getId(), 
				getThreadLocalRequest().getSession().getId(), 
				itemsCart.keySet());
		
		return true; //always return true for now
	}
}
