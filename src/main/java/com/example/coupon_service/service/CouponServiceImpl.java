package com.example.coupon_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.coupon_service.dto.CartDTO;
import com.example.coupon_service.dto.CouponDTO;
import com.example.coupon_service.dto.CouponDetailsDTO;
import com.example.coupon_service.entity.Coupon;
import com.example.coupon_service.entity.CouponType;
import com.example.coupon_service.entity.ProductQuantity;
import com.example.coupon_service.repository.CouponRepository;

@Service
public class CouponServiceImpl implements CouponService {

	private static final Logger log = LoggerFactory.getLogger(CouponServiceImpl.class);

//	private static final double DEFAULT_THRESHOLD = 0;

	@Autowired
	private CouponRepository couponRepository;

	@Override
	public Coupon createCoupon(CouponDTO couponDTO) {
		if(couponDTO == null || couponDTO.getDetails()== null) {
			throw new IllegalArgumentException("CouponDTO or CouponDetails cannot be null");
		}
		CouponDetailsDTO detailsDTO=couponDTO.getDetails();
		Double threshold = detailsDTO.getThreshold();
		if(threshold == null) {
			threshold = 0.0 ;
		}
		Coupon coupon = mapToEntity(couponDTO);
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> createMultipleCoupons(List<CouponDTO> couponDTOs) {
		List<Coupon> coupons = couponDTOs.stream().map(this::mapToEntity).collect(Collectors.toList());
		return couponRepository.saveAll(coupons);
	}

	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepository.findAll();
	}

	@Override
	public Coupon findCouponById(Long id) {
		log.info("Looking for coupon with ID: {}", id);
		return couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
	}

	@Override
	public List<Coupon> getApplicableCoupons(CartDTO cartDTO) {
		return couponRepository.findAll().stream().filter(coupon -> isCouponApplicable(coupon, cartDTO))
				.collect(Collectors.toList());
	}

	@Override
	public CartDTO applyCoupon(Long couponId, CartDTO cartDTO) {
		Coupon coupon = findCouponById(couponId);
		CartDTO updatedCart = applyCouponToCart(coupon, cartDTO);
		return updatedCart;
	}

	private Coupon mapToEntity(CouponDTO couponDTO) {
		Coupon coupon = new Coupon();

		coupon.setType(CouponType.valueOf(couponDTO.getType()));

		coupon.setThreshold(couponDTO.getDetails().getThreshold());
		coupon.setDiscount(couponDTO.getDetails().getDiscount());
		coupon.setProductId(couponDTO.getDetails().getProductId());
		coupon.setProductDiscount(couponDTO.getDetails().getProductDiscount());

		if (couponDTO.getDetails().getBuyProducts() != null) {
			coupon.setBuyProducts(couponDTO.getDetails().getBuyProducts().stream().map(dto -> {
				ProductQuantity pq = new ProductQuantity();
				pq.setProductId(dto.getProductId());
				pq.setQuantity(dto.getQuantity());
				return pq;
			}).collect(Collectors.toList()));;
		}

		if (couponDTO.getDetails().getGetProducts() != null) {
			coupon.setGetProducts(couponDTO.getDetails().getGetProducts().stream().map(dto -> {
				ProductQuantity pq = new ProductQuantity();
				pq.setProductId(dto.getProductId());
				pq.setQuantity(dto.getQuantity());
				return pq;
			}).collect(Collectors.toList()));;
		}

		coupon.setRepetitionLimit(couponDTO.getDetails().getRepetitionLimit());
		return coupon;
	}

	private boolean isCouponApplicable(Coupon coupon, CartDTO cartDTO) {
		Double threshold = coupon.getThreshold();
		Double discount= coupon.getDiscount();

		if (threshold == null || discount == null) {
			log.error("Coupon threshold or discount is null for coupon ID: "+ coupon.getId());
		    return false;
		}
		return cartDTO.getTotalPrice() >= threshold ;

	}

	private CartDTO applyCouponToCart(Coupon coupon, CartDTO cartDTO) {
		double totalDiscount = 0;
		double cartTotal = cartDTO.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		List<CartDTO.ItemDTO> updatedItems = new ArrayList<>();

		switch (coupon.getType()) {
		case CART_WISE:
			if (cartTotal >= coupon.getThreshold()) {
				totalDiscount = cartTotal * (coupon.getDiscount() / 100.0);
			}
			updatedItems.addAll(cartDTO.getItems());
			break;

		case PRODUCT_WISE:
			for (CartDTO.ItemDTO item : cartDTO.getItems()) {
				if (item.getProductId().equals(coupon.getProductId())) {
					double productTotal = item.getPrice() * item.getQuantity();
					double productDiscount = productTotal * (coupon.getDiscount() / 100.0);
					totalDiscount += productDiscount;

					CartDTO.ItemDTO updatedItem = new CartDTO.ItemDTO();
					updatedItem.setProductId(item.getProductId());
					updatedItem.setQuantity(item.getQuantity());
					updatedItem.setPrice(item.getPrice());
					updatedItem.setTotalDiscount(productDiscount);
					updatedItems.add(updatedItem);
				} else {
					CartDTO.ItemDTO unchangedItem = new CartDTO.ItemDTO();
					unchangedItem.setProductId(item.getProductId());
					unchangedItem.setQuantity(item.getQuantity());
					unchangedItem.setPrice(item.getPrice());
					unchangedItem.setTotalDiscount(0);
					updatedItems.add(unchangedItem);
				}
			}
			break;

		case BXGY:
			if (coupon.getGetProducts() != null) {
				for (ProductQuantity getProduct : coupon.getGetProducts()) {
					CartDTO.ItemDTO cartItem = cartDTO.getItems().stream()
							.filter(ci -> ci.getProductId().equals(getProduct.getProductId())).findFirst().orElse(null);

					if (cartItem != null) {
						double getProductDiscount = getProduct.getQuantity() * cartItem.getPrice();
						totalDiscount += getProductDiscount;

						CartDTO.ItemDTO updatedItem = new CartDTO.ItemDTO();
						updatedItem.setProductId(cartItem.getProductId());
						updatedItem.setQuantity(cartItem.getQuantity() + getProduct.getQuantity());
						updatedItem.setPrice(cartItem.getPrice());
						updatedItem.setTotalDiscount(getProductDiscount);
						updatedItems.add(updatedItem);
					}
				}
			}

			for (CartDTO.ItemDTO item : cartDTO.getItems()) {
				if (updatedItems.stream()
						.noneMatch(updatedItem -> updatedItem.getProductId().equals(item.getProductId()))) {
					CartDTO.ItemDTO unchangedItem = new CartDTO.ItemDTO();
					unchangedItem.setProductId(item.getProductId());
					unchangedItem.setQuantity(item.getQuantity());
					unchangedItem.setPrice(item.getPrice());
					unchangedItem.setTotalDiscount(0);
					updatedItems.add(unchangedItem);
				}
			}
			break;

		default:
			throw new IllegalArgumentException("Unknown coupon type: " + coupon.getType());
		}

		double finalPrice = cartTotal - totalDiscount;

		CartDTO updatedCart = new CartDTO();
		updatedCart.setItems(updatedItems);
		updatedCart.setTotalPrice(cartTotal);
		updatedCart.setTotalDiscount(totalDiscount);
		updatedCart.setFinalPrice(finalPrice);

		return updatedCart;
	}

	@Override
	public CouponDTO getCouponById(Long id) {
		Coupon coupon = couponRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

		CouponDTO couponDTO = new CouponDTO();

		CouponDetailsDTO details = new CouponDetailsDTO();
		details.setThreshold(coupon.getThreshold()); 
		details.setDiscount(coupon.getDiscount());

		couponDTO.setDetails(details);

		return couponDTO;
	}

	public void deleteCouponById(Long couponId) {
		Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("Coupon not found"));
		couponRepository.delete(coupon);
	}

}