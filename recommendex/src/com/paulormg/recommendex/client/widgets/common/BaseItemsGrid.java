package com.paulormg.recommendex.client.widgets.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.paulormg.recommendex.shared.transfer.Item;

public abstract class BaseItemsGrid extends Composite{

	protected static final String AVAILABLE_ITEMS = "Available items";	
	protected static final int NUM_COLUMNS = 3;		
	
	protected FlexTable itemsWidget;
	protected FlexTable itemsGrid;
	
	protected Map<Integer, Item> items;
	
	public BaseItemsGrid(String tableHeading){
		this.items = new HashMap<Integer, Item>();
		this.itemsGrid = new FlexTable();
		this.itemsWidget = new FlexTable();
		
		initWidget(itemsWidget);
		
		//Set Style
		itemsWidget.getFlexCellFormatter().addStyleName(0, 0, "sectionHeading");
		itemsGrid.setCellPadding(10);
		
		// Set text and widgets
		itemsWidget.setText(0, 0, tableHeading);		
		itemsWidget.setWidget(1, 0, itemsGrid);				
	}
	
	public void addItem(Item item) {
		items.put(item.getId(), item);
		refreshItemsGrid();
	}	

	public void removeItem(int itemId){
		items.remove(itemId);
		refreshItemsGrid();
	}	
	
	public void setItems(Map<Integer, Item> newItems){
		this.items = newItems;
		refreshItemsGrid();
	}
	
	private void refreshItemsGrid() {
		itemsGrid.clear();
		int i = 0;
		for (Item item : items.values()) {
			itemsGrid.setWidget(i/NUM_COLUMNS, i%NUM_COLUMNS, getItemWidget(item));
			i++;			
		}
	}	
	
	protected abstract Widget getItemWidget(Item item);	
	
}
