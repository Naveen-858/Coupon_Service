package com.example.coupon_service.service;

import java.util.List;

import com.example.coupon_service.dto.CartDTO;
import com.example.coupon_service.dto.CouponDTO;
import com.example.coupon_service.entity.Coupon;

public interface CouponService {
	Coupon createCoupon(CouponDTO couponDTO);

	List<Coupon> createMultipleCoupons(List<CouponDTO> couponDTOs);

	List<Coupon> findAllCoupons();

	Coupon findCouponById(Long id);

	List<Coupon> getApplicableCoupons(CartDTO cartDTO);

	CartDTO applyCoupon(Long couponId, CartDTO cartDTO);

	CouponDTO getCouponById(Long id);
}