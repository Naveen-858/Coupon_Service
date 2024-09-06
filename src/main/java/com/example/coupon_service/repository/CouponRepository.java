package com.example.coupon_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coupon_service.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}