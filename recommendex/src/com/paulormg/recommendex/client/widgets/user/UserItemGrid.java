package com.paulormg.recommendex.client.widgets.user;

import com.google.gwt.user.client.ui.Widget;
import com.paulormg.recommendex.client.widgets.common.BaseItemsGrid;
import com.paulormg.recommendex.shared.transfer.Item;

public class UserItemGrid extends BaseItemsGrid {
	
	private UserScreen parent;

	public UserItemGrid(UserScreen parent){
		this(parent, AVAILABLE_ITEMS);
	}
	
	public UserItemGrid(UserScreen parent,
							String tableHeading){
		super(tableHeading);
		this.parent = parent;
	}
	
	@Override
	protected Widget getItemWidget(Item item) {
		return new UserItemCell(parent, item);
	}

}
