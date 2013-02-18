package com.paulormg.recommendex.client.widgets.user;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.paulormg.recommendex.shared.transfer.Item;

public class ItemDetailsWidget extends Composite {
	
	private static final String RELATED_ITEMS = "Related items";

	private UserScreen parent;
		
	private FlexTable mainTable;
	private FlexTable itemDetails;

	private Item item;
	
	public ItemDetailsWidget(UserScreen parent, Item item){
		this.parent = parent;
		this.item = item;
		
		mainTable = new FlexTable();
		itemDetails = new FlexTable();
		
		initWidget(mainTable);
		
		setStyle();	
		
		setTables(item);
	}
	
	/* USER INTERFACE METHODS */

	public void setRelatedItems(int itemId, Map<Integer, Item> items){
		if (!items.isEmpty() && itemId == item.getId()){
			UserItemGrid recommendedItems = new UserItemGrid(parent, RELATED_ITEMS);
			recommendedItems.setItems(items);
			mainTable.setWidget(2, 0, recommendedItems);
		}
	}
	
	private void setTables(Item item) {
		itemDetails.setWidget(0, 0, getItemImg(item));
		itemDetails.setText(1, 0, item.getName());
		itemDetails.setText(2, 0, item.getDesc());
		itemDetails.setWidget(3, 0, getAddToShoppingCartAnchor(item));
		itemDetails.setWidget(4, 0, getReturnAnchor());
		
		mainTable.setText(0, 0, "Item details");		
		mainTable.setWidget(1, 0, itemDetails);
		mainTable.setWidget(2, 0, getNoRecommendationsWidget());
	}

	private Widget getNoRecommendationsWidget() {
		Grid grid = new Grid(2, 1);

		grid.getCellFormatter().setStyleName(0, 0, "sectionHeading");
		grid.getCellFormatter().setStyleName(1, 0, "serverResponseLabelError");
		
		
		grid.setText(0, 0, RELATED_ITEMS);
		grid.setText(1, 0, "No related items yet.");
		
		
		return grid;
	}

	private void setStyle() {
		mainTable.getFlexCellFormatter().addStyleName(0, 0, "sectionHeading");
		mainTable.getFlexCellFormatter().setHorizontalAlignment(1, 0, 
												HasHorizontalAlignment.ALIGN_CENTER);
		
		itemDetails.setStyleName("itemDetails");
		itemDetails.getFlexCellFormatter().addStyleName(1, 0, "itemTitle");
		itemDetails.getFlexCellFormatter().addStyleName(2, 0, "itemDesc");		
		itemDetails.getFlexCellFormatter().addStyleName(3, 0, "itemAction");
	}

	private Image getItemImg(Item item) {
		Image itemImg = new Image(item.getImgUrl());		
		itemImg.setSize("200px", "200px");
		return itemImg;
	}

	private Anchor getReturnAnchor() {
		Anchor returnAnchor = new Anchor("Go back to shop", "#");
		returnAnchor.addClickHandler(getReturnClickHandler());
		return returnAnchor;
	}

	public Anchor getAddToShoppingCartAnchor(Item item) {
		Anchor addToCartLink = new Anchor("Add to shopping cart","#");
		addToCartLink.addClickHandler(getAddtoCartClickHandler(item));
		return  addToCartLink;
	}

	private ClickHandler getAddtoCartClickHandler(final Item item) {
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				parent.addToShoppingCart(item);
			}
		};
	}	
	
	private ClickHandler getReturnClickHandler() {
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				parent.showItemsGrid();
			}
		};
	}
	
	/* CLIENT METHODS */

	public Item getItem(){
		return this.item;
	}
	
}
