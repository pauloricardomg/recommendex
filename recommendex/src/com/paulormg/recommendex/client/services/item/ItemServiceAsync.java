package com.paulormg.recommendex.client.services.item;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paulormg.recommendex.shared.transfer.Item;

public interface ItemServiceAsync {

	void addItem(String login, Item item, AsyncCallback<Void> callback);

	void removeItem(String login, int itemId, AsyncCallback<Boolean> callback);
	
	void getItems(String login, AsyncCallback<Map<Integer, Item>> callback);

	void checkout(String login, Map<Integer, Integer> itemsCart,
					AsyncCallback<Boolean> callback);

	void getRelatedItems(String login, int itemId,
			AsyncCallback<Map<Integer, Item>> callback);

}
