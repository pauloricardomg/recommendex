package com.paulormg.recommendex.client.widgets.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.paulormg.recommendex.shared.transfer.Item;

public class AdminItemCell extends Composite {

	private AdminScreen parent;

	public AdminItemCell(AdminScreen parent, Item item){
		this.parent = parent;

		// Init widgets
		FlexTable itemTable = new FlexTable();
		Image itemImg = new Image(item.getImgUrl());
		initWidget(itemTable);

		// Setting style
		itemImg.setSize("60px", "60px");		
		
		itemTable.setStyleName("itemsCell");		
		itemTable.getFlexCellFormatter().addStyleName(1, 0, "itemTitle");
		itemTable.getFlexCellFormatter().addStyleName(2, 0, "itemDesc");		
		itemTable.getFlexCellFormatter().addStyleName(3, 0, "itemAction");

		// Setting item table
		itemTable.setWidget(0, 0, itemImg);
		itemTable.setText(1, 0, item.getName());
		itemTable.setText(2, 0, item.getDesc());
		itemTable.setWidget(3, 0, getRemoveAnchor(item));			
	}
	
	public Anchor getRemoveAnchor(Item item) {
		Anchor removeLink = new Anchor("Remove","#");
		removeLink.addClickHandler(getRemoveClickHandler(item));
		return removeLink;
	}

	private ClickHandler getRemoveClickHandler(final Item item) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.serverRemoveItem(item);
			}
		};
	}	
}