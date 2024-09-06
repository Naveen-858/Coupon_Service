package com.example.coupon_service.dto;

import java.util.List;

public class CouponDetailsDTO {

	private Double threshold;
	private Double discount;
	private Long productId;
	private Double productDiscount;
	private List<ProductQuantityDTO> buyProducts;
	private List<ProductQuantityDTO> getProducts;
	private Integer repetitionLimit;
	public Double getThreshold() {
		return threshold;
	}
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Double getProductDiscount() {
		return productDiscount;
	}
	public void setProductDiscount(Double productDiscount) {
		this.productDiscount = productDiscount;
	}
	public List<ProductQuantityDTO> getBuyProducts() {
		return buyProducts;
	}
	public void setBuyProducts(List<ProductQuantityDTO> buyProducts) {
		this.buyProducts = buyProducts;
	}
	public List<ProductQuantityDTO> getGetProducts() {
		return getProducts;
	}
	public void setGetProducts(List<ProductQuantityDTO> getProducts) {
		this.getProducts = getProducts;
	}
	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}
	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}
	
	

}
