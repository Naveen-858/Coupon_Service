package com.example.coupon_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coupon_service.dto.CartDTO;
import com.example.coupon_service.dto.CouponDTO;
import com.example.coupon_service.entity.Coupon;
import com.example.coupon_service.service.CouponService;

@RestController
@RequestMapping("/coupons")
public class CouponController {

	private static final Logger log = LoggerFactory.getLogger(CouponController.class);

	@Autowired
	private CouponService couponService;

	public CouponController(CouponService couponService) {
		super();
		this.couponService = couponService;
	}

	@PostMapping
	public ResponseEntity<Coupon> createCoupon(@RequestBody CouponDTO couponDTO) {
		Coupon createdCoupon = couponService.createCoupon(couponDTO);
		return ResponseEntity.ok(createdCoupon);
	}

	@PostMapping("/bulk-create")
	public ResponseEntity<List<Coupon>> createMultipleCoupons(@RequestBody List<CouponDTO> couponDTOs) {
		List<Coupon> createdCoupons = couponService.createMultipleCoupons(couponDTOs);
		return ResponseEntity.ok(createdCoupons);
	}

	@GetMapping
	public ResponseEntity<List<Coupon>> getAllCoupons() {
		List<Coupon> coupons = couponService.findAllCoupons();
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
		Coupon coupon = couponService.findCouponById(id);
		return ResponseEntity.ok(coupon);
	}

	@PostMapping("/applicable-coupons")
	public ResponseEntity<List<Coupon>> getApplicableCoupons(@RequestBody CartDTO cartDTO) {
		List<Coupon> applicableCoupons = couponService.getApplicableCoupons(cartDTO);
		return ResponseEntity.ok(applicableCoupons);
	}

	@PostMapping("/apply-coupon/{id}")
	public ResponseEntity<CartDTO> applyCoupon(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
		try {

			CouponDTO couponDTO = couponService.getCouponById(id);

			if (couponDTO.getDetails() == null || couponDTO.getDetails().getThreshold() == null) {
				throw new RuntimeException("Coupon details or threshold are missing for coupon with id: " + id);
			}

			CartDTO updatedCart = couponService.applyCoupon(id, cartDTO);
			return ResponseEntity.ok(updatedCart);

		} catch (RuntimeException e) {

			log.error("Error applying coupon: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}