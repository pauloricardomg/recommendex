package com.paulormg.recommendex.client.widgets.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.paulormg.recommendex.shared.transfer.Item;

public class AddItemWidget extends Composite {

	private static final String SAMPLE_IMG = "http://2.bp.blogspot.com/-s7c_8Z3lpjg/" +
			"TaL210cpo6I/AAAAAAAADlQ/S4mcXaVwB6E/s16" +
			"00/hobbit9.jpg";

	private FlexTable mainTable;
	
	private TextBox itemName;
	private TextBox itemDesc;	
	private FileUpload imgUpload;
	private Button addItem;
	
	private AdminScreen parent;

	public AddItemWidget(AdminScreen parent){
		this.parent = parent;
		
		mainTable = new FlexTable();
		itemName = new TextBox();
		itemDesc = new TextBox();	
		imgUpload = new FileUpload();
		addItem = setAddItemButton();		
		initWidget(mainTable);

		setStyle();

		setWidget();
	}

	public void clear(){
		itemName.setText("");
		itemDesc.setText("");
		imgUpload = new FileUpload();
	}
	
	private Button setAddItemButton() {
		Button button = new Button("Add Item");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Item item = new Item(itemName.getText(), itemDesc.getText(), SAMPLE_IMG);
				parent.serverAddItem(item);
			}
		});
		return button;
	}	
	
	private void setStyle() {
		mainTable.setStyleName("addItemTable");
		
		itemName.setWidth("150px");
		itemDesc.setWidth("150px");

		mainTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		mainTable.getFlexCellFormatter().setColSpan(4, 0, 2);
		mainTable.getFlexCellFormatter().setHorizontalAlignment(
				4, 0, HasHorizontalAlignment.ALIGN_CENTER);
		mainTable.getFlexCellFormatter().addStyleName(4, 0, "paddedCell");		

		mainTable.getFlexCellFormatter().addStyleName(0, 0, "sectionHeading");
	}	
	
	private void setWidget() {
		mainTable.setText(0, 0, "Add product");		
		
		mainTable.setText(1, 0, "Product name:");
		mainTable.setWidget(1, 1, itemName);

		mainTable.setText(2, 0, "Product description:");
		mainTable.setWidget(2, 1, itemDesc);

		mainTable.setText(3, 0, "Product image:");
		mainTable.setWidget(3, 1, imgUpload);

		mainTable.setWidget(4, 0, addItem);		
	}
}
