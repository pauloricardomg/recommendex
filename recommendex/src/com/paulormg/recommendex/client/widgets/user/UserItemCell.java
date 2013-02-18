package com.paulormg.recommendex.client.widgets.user;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.paulormg.recommendex.shared.transfer.Item;

public class UserItemCell extends Composite {

	private UserScreen parent;

	public UserItemCell(UserScreen parent, Item item){
		this.parent = parent;

		FlexTable itemsTable = new FlexTable();
		Image itemImg = new Image(item.getImgUrl());		
		Anchor viewDetails = new Anchor("View details", "#");

		initWidget(itemsTable);

		viewDetails.addClickHandler(getDetailsClickHandler(item));

		// Setting style
		itemsTable.setStyleName("itemsCell");		
		itemsTable.getFlexCellFormatter().addStyleName(1, 0, "itemTitle");
		itemsTable.getFlexCellFormatter().addStyleName(3, 0, "itemAction");
		itemImg.setSize("60px", "60px");
		
		// Setting item table
		itemsTable.setWidget(0, 0, itemImg);
		itemsTable.setText(1, 0, item.getName());
		itemsTable.setWidget(2, 0, viewDetails);
		itemsTable.setWidget(3, 0, getAddToShoppingCartAnchor(item));			
	}
	
	public Anchor getAddToShoppingCartAnchor(Item item) {
		Anchor addToCartLink = new Anchor("Add to shopping cart","#");
		addToCartLink.addClickHandler(getAddtoCartClickHandler(item));
		return  addToCartLink;
	}
	
	/* CLICK HANDLERS */
	
	private ClickHandler getDetailsClickHandler(final Item item) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				parent.viewItemDetails(item);		
			}
		};
	}

	private ClickHandler getAddtoCartClickHandler(final Item item) {
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				parent.addToShoppingCart(item);
			}
		};
	}
}