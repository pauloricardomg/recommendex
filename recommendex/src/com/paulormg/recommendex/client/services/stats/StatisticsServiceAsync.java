package com.paulormg.recommendex.client.services.stats;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatisticsServiceAsync {

	void addedItemToCart(int id, AsyncCallback<Void> callback);

	void viewedItem(int id, AsyncCallback<Void> callback);

}
