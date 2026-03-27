package com.example.secondhand.controller;

import com.example.secondhand.entity.CartItem;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<CartItem> items = cartService.listByUserId(userId);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(
            HttpServletRequest request,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Long userId = getCurrentUserId(request);
        cartService.add(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateQuantity(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        Long userId = getCurrentUserId(request);
        cartService.updateQuantity(id, userId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long userId = getCurrentUserId(request);
        cartService.remove(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        cartService.clear(userId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null) {
                return parsed.userId();
            }
        }
        throw new SecurityException("Invalid token");
    }
}
