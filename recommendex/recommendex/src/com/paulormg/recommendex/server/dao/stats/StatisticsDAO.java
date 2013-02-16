package com.paulormg.recommendex.server.dao.stats;

import java.util.Set;


/*
 * Ideally all DAOS should be interfaces instantianted and
 * fetched by dependency injection, but let's keep it simple
 * for now.
 */
public class StatisticsDAO {

	// Singleton
	private static StatisticsDAO instance = null;

	public static StatisticsDAO getInstance(){
		if (instance == null){
			instance = new StatisticsDAO();
		}

		return instance;
	}

	public void viewedItem(int userId, String sessionId, int itemId) {
		//Register action on Cassandra
	}

	public void addedItemToCart(int userId, String sessionId, int itemId) {
		//Register action on Cassandra
	}
	
	public void boughtItems(int userId, String sessionId, Set<Integer> items) {
		//Register action on Cassandra
	}

	public int[] getRelatedItems(int itemId, int max) {
		int[] result = new int[max];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		return result;
	}
}
