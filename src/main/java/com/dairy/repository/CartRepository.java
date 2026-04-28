package com.dairy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.dairy.model.Cart;
import com.dairy.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	List<Cart> findByUser(User user);

	Optional<Cart> findByUserAndProductId(User user, Long productId);

	@Modifying
	void deleteByUser(User user);
}