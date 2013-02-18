package com.paulormg.recommendex.client.widgets.user;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.paulormg.recommendex.client.Recommendex;
import com.paulormg.recommendex.client.services.stats.StatisticsService;
import com.paulormg.recommendex.client.services.stats.StatisticsServiceAsync;
import com.paulormg.recommendex.client.util.ClientUtils;
import com.paulormg.recommendex.client.widgets.common.BaseItemsGrid;
import com.paulormg.recommendex.client.widgets.common.BaseScreen;
import com.paulormg.recommendex.shared.transfer.Item;

public class UserScreen extends BaseScreen {
	
	private final StatisticsServiceAsync statsService = 
			   GWT.create(StatisticsService.class);	

	private ShoppingCartWidget shoppingCart;
	private ItemDetailsWidget itemDetails;
	
	public UserScreen(Recommendex parent, Map<Integer, Item> items){
		super(parent);
		
		// Init widgets
		shoppingCart = new ShoppingCartWidget(this);
		mainTable.getFlexCellFormatter().setHorizontalAlignment
						(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		mainTable.setWidget(1, 0, shoppingCart);
		
		super.init(items);
	}
	
	public void showItemsGrid(){
		itemDetails = null;
		changeScreen(itemsGrid);
	}
	
	public void addToShoppingCart(Item item) {
		shoppingCart.addToShoppingCart(item);
	}	

	public void viewItemDetails(Item item){
		clearAll();
		statsService.viewedItem(item.getId(), ClientUtils.getStatisticsCallback());
		itemService.getRelatedItems(getLogin(), item.getId(), 
										getRelatedItemsCallback(item));
		itemDetails = new ItemDetailsWidget(this, item);
		changeScreen(itemDetails);
	}

	@Override
	protected BaseItemsGrid createItemsGrid() {
		return new UserItemGrid(this);
	}
	
	private AsyncCallback<Map<Integer, Item>> getRelatedItemsCallback(final Item item) {
		return new AsyncCallback<Map<Integer,Item>>() {

			@Override
			public void onFailure(Throwable caught) {
				showInfoMessage("Error while retrieving related items for item " + item.getName());
			}

			@Override
			public void onSuccess(Map<Integer, Item> result) {
				if (itemDetails != null){
					itemDetails.setRelatedItems(item.getId(), result);
				}
			}
		};
	}	
}
