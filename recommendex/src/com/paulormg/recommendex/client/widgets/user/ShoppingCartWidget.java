package com.paulormg.recommendex.client.widgets.user;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.paulormg.recommendex.client.services.item.ItemService;
import com.paulormg.recommendex.client.services.item.ItemServiceAsync;
import com.paulormg.recommendex.client.services.stats.StatisticsService;
import com.paulormg.recommendex.client.services.stats.StatisticsServiceAsync;
import com.paulormg.recommendex.client.util.ClientUtils;
import com.paulormg.recommendex.shared.transfer.Item;

public class ShoppingCartWidget extends Composite {

	private static final int NAME_COL = 0;
	private static final int QNT_COL = 1;
	private static final int ADD_COL = 2;
	private static final int DEL_COL = 3;

	protected ItemServiceAsync itemService = GWT.create(ItemService.class);	
	protected StatisticsServiceAsync statsService = GWT.create(StatisticsService.class);		
	
	private UserScreen parent;	

	private FlexTable mainTable;
	private FlexTable cartTable;

	private Map<Integer, Integer> shoppingCart;
	private LinkedList<Integer> row2item; //maps table rows (list index) to item ID

	public ShoppingCartWidget(UserScreen parent){
		this.parent = parent;

		this.mainTable = new FlexTable();
		this.cartTable = new FlexTable();
		
		
		this.shoppingCart = new HashMap<Integer, Integer>();
		this.row2item = new LinkedList<Integer>();
		initWidget(mainTable);

		setStyle();		
		
		setWidget();

		row2item.add(-1); //table header (row 0)
	}

	private void setWidget() {
		Button checkoutButton = new Button("Checkout");
		checkoutButton.addClickHandler(getCheckoutClickHandler());		
		
		cartTable.setText(0, 0, "Item");
		cartTable.setText(0, 1, "Quantity");
		cartTable.setText(0, 2, "");
		cartTable.setText(0, 3, "");
		
		mainTable.setText(0, 0, "Shopping cart");	
		mainTable.setWidget(1, 0, cartTable);
		mainTable.setWidget(2, 0, checkoutButton);	
	}

	private void setStyle() {
		mainTable.getFlexCellFormatter().addStyleName(0, 0, "sectionHeading");
		mainTable.getFlexCellFormatter().addStyleName(2, 0, "paddedCell");
		mainTable.getFlexCellFormatter().setHorizontalAlignment(
				1, 0, HasHorizontalAlignment.ALIGN_CENTER);			
		mainTable.getFlexCellFormatter().setHorizontalAlignment(
				2, 0, HasHorizontalAlignment.ALIGN_CENTER);		

		cartTable.setWidth("400px");
		cartTable.getRowFormatter().addStyleName(0, "shoppingCartHeader");
		cartTable.getColumnFormatter().setWidth(0, "50%");
		cartTable.getColumnFormatter().setWidth(1, "20%");
		cartTable.getColumnFormatter().setWidth(2, "15%");
		cartTable.getColumnFormatter().setWidth(2, "15%");
	}

	/* USER INTERFACE METHODS */		
	
	public void addToShoppingCart(Item item){ 
		int itemCount = 0;
		int itemRow;

		int itemId = item.getId();
		
		if (shoppingCart.containsKey(itemId)){
			itemCount = shoppingCart.get(itemId);
			itemRow = row2item.indexOf(itemId); // the position on the list
												// is the row on the table
		} else {
			itemRow = cartTable.getRowCount();
			cartTable.setText(itemRow, NAME_COL, item.getName());
			row2item.add(itemId);
		}

		cartTable.setText(itemRow, QNT_COL, ++itemCount + "");
		cartTable.setWidget(itemRow, ADD_COL, getAddItemLink(item));
		cartTable.setWidget(itemRow, DEL_COL, getRemoveItemLink(item));

		parent.clearAll();
		statsService.addedItemToCart(item.getId(), ClientUtils.getStatisticsCallback());		
		
		shoppingCart.put(itemId, itemCount);
	}

	public Anchor getAddItemLink(final Item item){
		ClickHandler addHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addToShoppingCart(item);
			}
		};

		Anchor addItem = new Anchor("Add", "#");
		addItem.addClickHandler(addHandler);

		return addItem;
	}

	public Anchor getRemoveItemLink(final Item item){
		ClickHandler removeHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeFromShoppingCart(item);
			}
		};

		Anchor removeItem = new Anchor("Remove", "#");
		removeItem.addClickHandler(removeHandler);

		return removeItem;
	}	
	
	public void removeFromShoppingCart(Item item){
		int itemId = item.getId();
		
		if (shoppingCart.containsKey(itemId)){
			int itemCount = shoppingCart.get(itemId);
			int itemRow = row2item.indexOf(itemId);
			if (itemCount > 1){
				cartTable.setText(itemRow, QNT_COL, --itemCount + "");
				shoppingCart.put(itemId, itemCount);				
			} else {
				row2item.remove(itemRow);
				shoppingCart.remove(itemId);
				cartTable.removeRow(itemRow);
			}
		}
	}

	private ClickHandler getCheckoutClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (shoppingCart.isEmpty()){
					Window.alert("Shopping cart is empty.");
				} else {
					checkout(shoppingCart);					
				}
			}
		};
	}

	public void clear() {
		this.shoppingCart.clear();
		int rowCount = cartTable.getRowCount();
		for (int i=0; i < rowCount-1; i++){
			row2item.remove(1);
			cartTable.removeRow(1);
		}
	}
	
	/* SERVER ACTIONS */
	
	public void checkout(Map<Integer, Integer> itemsCart){
		parent.clearAll();
		itemService.checkout(parent.getLogin(), itemsCart, getCheckoutCallback());
	}

	/* ASYNC CALLBACKS */	

	private AsyncCallback<Boolean> getCheckoutCallback() {
		return new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				parent.showInfoMessage("Server error while perfoming checkout. Please try again.");
			}

			@Override
			public void onSuccess(Boolean success) {
				if (success){
					clear();				
					parent.showInfoMessage("Checkout succesfully done.");
				} else {
					parent.showInfoMessage("Could not complete checkout. Please try again.");
				}
			}
		};
	}	
}
