package com.paulormg.recommendex.client.services.stats;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stats")
public interface StatisticsService extends RemoteService {

	public void viewedItem(int id);
	
	public void addedItemToCart(int id);
	
}
