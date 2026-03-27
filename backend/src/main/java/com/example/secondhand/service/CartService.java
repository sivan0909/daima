package com.example.secondhand.service;

import com.example.secondhand.entity.CartItem;
import com.example.secondhand.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public List<CartItem> listByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void add(Long userId, Long productId, Integer quantity) {
        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            cartItemRepository.save(existing.get());
        } else {
            CartItem item = new CartItem();
            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void updateQuantity(Long id, Long userId, Integer quantity) {
        cartItemRepository.findById(id).ifPresent(item -> {
            cartItemRepository.save(item);
        });
    }

    @Transactional
    public void remove(Long id, Long userId) {
        cartItemRepository.findById(id).ifPresent(item -> {
            cartItemRepository.delete(item);
        });
    }

    @Transactional
    public void clear(Long userId) {
        // 简化实现，不调用 deleteByUserId 方法
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        for (CartItem item : items) {
            cartItemRepository.delete(item);
        }
    }
}
