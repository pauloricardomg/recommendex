package com.paulormg.recommendex.shared.transfer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Item implements Serializable {

	private int id;
	private String name;
	private String desc;
	private String imgUrl;
	private List<Integer> relatedItems;

	public Item(){
		//required by gwt sdk
	}
	
	public Item(String name, String desc, String imgUrl){
		this(-1, name, desc, imgUrl);
	}	
	
	public Item(int id, String name, String desc, String imgUrl){
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.imgUrl = imgUrl;
		this.relatedItems = new LinkedList<Integer>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Integer> getRelatedItems() {
		return relatedItems;
	}

	public void setRelatedItems(List<Integer> relatedItem) {
		this.relatedItems = relatedItem;
	}
}
