package com.paulormg.recommendex.client.widgets.admin;

import com.google.gwt.user.client.ui.Widget;
import com.paulormg.recommendex.client.widgets.common.BaseItemsGrid;
import com.paulormg.recommendex.shared.transfer.Item;

public class AdminItemGrid extends BaseItemsGrid {

	private AdminScreen parent;

	public AdminItemGrid(AdminScreen parent){
		this(parent, AVAILABLE_ITEMS);
	}	
	
	public AdminItemGrid(AdminScreen parent, 
							String tableHeading){
		super(tableHeading);
		this.parent = parent;
	}
	
	@Override
	protected Widget getItemWidget(Item item) {
		return new AdminItemCell(parent, item);
	}

}
