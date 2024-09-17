package com.example.coupon_service.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private CouponType type;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "threshold")
	private Double threshold = 0.0;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "product_discount")
	private Double productDiscount;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "coupon_id")
	private List<ProductQuantity> buyProducts;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "coupon_id")
	private List<ProductQuantity> getProducts;

	@Column(name = "repition_limit")
	private Integer repetitionLimit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
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

	public List<ProductQuantity> getBuyProducts() {
		return buyProducts;
	}

	public void setBuyProducts(List<ProductQuantity> buyProducts) {
		this.buyProducts = buyProducts;
	}

	public List<ProductQuantity> getGetProducts() {
		return getProducts;
	}

	public void setGetProducts(List<ProductQuantity> getProducts) {
		this.getProducts = getProducts;
	}

	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}

	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}

}