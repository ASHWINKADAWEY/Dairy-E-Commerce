package com.dairy.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequest {

	@NotBlank(message = "Delivery address is required")
	private String deliveryAddress;

	public OrderRequest() {
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String address) {
		this.deliveryAddress = address;
	}
}