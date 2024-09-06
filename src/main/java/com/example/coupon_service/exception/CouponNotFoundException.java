package com.example.coupon_service.exception;

public class CouponNotFoundException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	 public CouponNotFoundException(String message) {
	        super(message);
	    }
}