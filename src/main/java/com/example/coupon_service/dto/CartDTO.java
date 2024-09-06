package com.example.coupon_service.dto;

import java.util.List;

public class CartDTO {
	private List<ItemDTO> items;
	private double totalPrice;
	private double totalDiscount;
	private double finalPrice;

	
	
	 public List<ItemDTO> getItems() {
		return items;
	}



	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}



	public double getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}



	public double getTotalDiscount() {
		return totalDiscount;
	}



	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}



	public double getFinalPrice() {
		return finalPrice;
	}



	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}



	public static class ItemDTO {

	        private Long productId;
	        private int quantity;
	        private double price;
	        private double totalDiscount;
	        
			public Long getProductId() {
				return productId;
			}
			public void setProductId(Long productId) {
				this.productId = productId;
			}
			public int getQuantity() {
				return quantity;
			}
			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}
			public double getPrice() {
				return price;
			}
			public void setPrice(double price) {
				this.price = price;
			}
			public double getTotalDiscount() {
				return totalDiscount;
			}
			public void setTotalDiscount(double totalDiscount) {
				this.totalDiscount = totalDiscount;
			}
	        
	        
	
	
	}
}
