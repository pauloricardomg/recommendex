package com.paulormg.recommendex.client.widgets.common;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.paulormg.recommendex.client.Recommendex;
import com.paulormg.recommendex.client.services.item.ItemService;
import com.paulormg.recommendex.client.services.item.ItemServiceAsync;
import com.paulormg.recommendex.shared.transfer.Item;

public abstract class BaseScreen extends Composite {

	protected static final int NUM_COLUMNS = 3;		
	
	protected ItemServiceAsync itemService = GWT.create(ItemService.class);		
	
	protected BaseItemsGrid itemsGrid;
	protected FlexTable mainTable;
	
	protected Recommendex parent;
	
	public BaseScreen(Recommendex parent){
		this.parent = parent;
		
		this.mainTable = new FlexTable();
		this.itemsGrid = createItemsGrid();
		
		initWidget(mainTable);
		
		// Set style
		mainTable.setStyleName("mainScreen");
		mainTable.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	
		mainTable.setWidget(0, 0, itemsGrid); // Base Item Screen contains only the items widget
												// but subclasses may add more widgets below
	}
	
	/* USER INTERFACE ACTIONS */		
	
	/**
	 * Method be called by every sub-class upon instantiation
	 */
	public void init(Map<Integer, Item> items){
		this.itemsGrid.setItems(items);
	}
	
	protected void changeScreen(Widget newScreen){
		mainTable.setWidget(0, 0, newScreen);			
	}
	
	public void showInfoMessage(String message){
		parent.showInfoMessage(message);		
	}
	
	public void clearAll() {
		parent.clearInfoLabel();
	}		

	/* CLIENT ACTIONS */	
	
	public String getLogin(){
		return parent.getLogin();		
	}
	
	/* SERVER ACTIONS */
	
	public void serverGetItems(){
		itemService.getItems(getLogin(), getFetchItemsCallback());
	}		
	
	/* ASYNC CALLBACKS */	
	
	private AsyncCallback<Map<Integer, Item>> getFetchItemsCallback() {
		return new AsyncCallback<Map<Integer, Item>>() {

			@Override
			public void onSuccess(Map<Integer, Item> result) {
				itemsGrid.setItems(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				showInfoMessage("Error when trying to fetch items");
			}
		};
	}

	/* ABSTRACT METHODS */		
	
	protected abstract BaseItemsGrid createItemsGrid();
	
}
