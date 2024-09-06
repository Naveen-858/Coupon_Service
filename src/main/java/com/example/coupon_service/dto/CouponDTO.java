package com.example.coupon_service.dto;

public class CouponDTO {
	private String type;
	private CouponDetailsDTO details;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CouponDetailsDTO getDetails() {
        return details;
    }

    public void setDetails(CouponDetailsDTO details) {
        this.details = details;
    }

}