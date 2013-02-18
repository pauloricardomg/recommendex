package com.paulormg.recommendex.client.widgets.admin;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paulormg.recommendex.client.Recommendex;
import com.paulormg.recommendex.client.widgets.common.BaseItemsGrid;
import com.paulormg.recommendex.client.widgets.common.BaseScreen;
import com.paulormg.recommendex.shared.transfer.Item;

public class AdminScreen extends BaseScreen {

	private AddItemWidget addItemWidget = null;

	public AdminScreen(Recommendex parent, Map<Integer, Item> items){
		super(parent);

		// Init widgets
		addItemWidget = new AddItemWidget(this);
		mainTable.setWidget(1, 0, addItemWidget);
		
		super.init(items);
	}

	/* USER INTERFACE ACTIONS */		
	@Override
	protected BaseItemsGrid createItemsGrid() {
		return new AdminItemGrid(this);
	}		

	@Override
	public void clearAll() {
		super.clearAll();
		addItemWidget.clear();
	}
	
	/* SERVER ACTIONS */
	
	public void serverRemoveItem(Item item) {
		if (item != null){
			AsyncCallback<Boolean> removeCallBack = getRemoveItemCallback(item);
			itemService.removeItem(getLogin(), item.getId(), removeCallBack);
		}
	}	
	
	public void serverAddItem(Item item) {
		itemService.addItem(getLogin(), item, getAddItemCallback(item));
	}	
	
	/* ASYNC CALLBACKS */	
	
	private AsyncCallback<Void> getAddItemCallback(final Item item) {
		return new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				parent.showInfoMessage("Server error while adding item " + item.getName());
			}

			@Override
			public void onSuccess(Void result) {
				itemsGrid.addItem(item);
				parent.showInfoMessage("Item '" + item.getName() + "' successfully added.");
				clearAll();
				serverGetItems();		
			}
		};
	}	
	
	private AsyncCallback<Boolean> getRemoveItemCallback(final Item item) {
		return new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				parent.showInfoMessage("Server error while removing item " + item.getName());
			}

			@Override
			public void onSuccess(Boolean success) {
				if (success){
					itemsGrid.removeItem(item.getId());
					parent.showInfoMessage("Item '" + item.getName() + "' successfully removed.");					
				} else { 
					parent.showInfoMessage("Item does not exist.");										
				}
				serverGetItems();
			}
		};
	}
}
