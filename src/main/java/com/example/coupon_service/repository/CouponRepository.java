package com.example.coupon_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.coupon_service.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	  
    List<Coupon> findByType(String type);



  }