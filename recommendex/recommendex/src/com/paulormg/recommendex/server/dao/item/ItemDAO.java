package com.paulormg.recommendex.server.dao.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.paulormg.recommendex.shared.transfer.Item;

/*
 * Ideally all DAOS should be interfaces instantianted and
 * fetched by dependency injection, but let's keep it simple
 * for now.
 */
public class ItemDAO {

	// Singleton
	private static ItemDAO instance = null;

	public static ItemDAO getInstance(){
		if (instance == null){
			instance = new ItemDAO();
		}
		
		return instance;
	}
	
	private Map<Integer, Item> items;
	private int counter;
	
	public ItemDAO(){
		this.items = new HashMap<Integer, Item>();
		this.counter = 6;
		items.put(0, new Item(0, "action book 1", "Very nice action book", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
		items.put(1, new Item(1, "action book 2", "Very nice action book", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
		items.put(2, new Item(2, "novel 1", "not so nice novel", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
		items.put(3, new Item(3, "novel 2", "interesting novel", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
		items.put(4, new Item(4, "business book", "cool book for MBAs", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
		items.put(5, new Item(5, "entrepreneur book", "nice book", 
				"http://2.bp.blogspot.com/-s7c_8Z3lpjg/TaL210cpo6I/AAA" +
				"AAAAADlQ/S4mcXaVwB6E/s1600/hobbit9.jpg"));
	}
	
	/* ITEMS */	
	
	public Map<Integer, Item> getAllItems(){
		return new HashMap<Integer, Item>(this.items);
	}
	
	public Map<Integer, Item> getItems(int[] ids){
		HashMap<Integer, Item> result = new HashMap<Integer, Item>();
		for (int i = 0; i < ids.length; i++) {
			result.put(ids[i], items.get(ids[i]));
		}
		return result;
	}
	
	public boolean removeItem(int id){
		return this.items.remove(id) != null;
	}

	public int addItem(Item item) {
		item.setId(counter);
		this.items.put(counter, item);
		
		return counter++;
	}
	
	/* PURCHASES */

	public void checkout(String login, Map<Integer, Integer> itemsCart) {
		// Persist purchase on DB
	}
	
	public Collection<Integer> getRecentPurchases(int quantity){
		return new LinkedList<Integer>();
	}
}
