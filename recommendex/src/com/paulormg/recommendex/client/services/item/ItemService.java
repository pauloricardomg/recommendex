package com.paulormg.recommendex.client.services.item;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.exception.ServerError;
import com.paulormg.recommendex.shared.transfer.Item;
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("item")
public interface ItemService extends RemoteService {

	void addItem(String login, Item item) throws InvalidCredentialsException, ServerError;
	
	boolean removeItem(String login, int itemId) throws InvalidCredentialsException, ServerError;
	
	Map<Integer, Item> getItems(String login) throws InvalidCredentialsException, ServerError;
	
	Map<Integer, Item> getRelatedItems(String login, int itemId) throws InvalidCredentialsException, ServerError;	
	
	boolean checkout(String login, Map<Integer, Integer> itemsCart) throws InvalidCredentialsException, ServerError;
}
