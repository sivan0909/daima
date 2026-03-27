package com.example.secondhand.service;

import com.example.secondhand.entity.User;
import com.example.secondhand.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findPage(int page, int size, String keyword, String role, String status) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = "%" + keyword.trim() + "%";
                predicates.add(cb.or(
                        cb.like(root.get("username"), k),
                        cb.like(root.get("nickname"), k),
                        cb.like(root.get("phone"), k)
                ));
            }
            if (role != null && !role.trim().isEmpty() && !"全部".equals(role)) {
                predicates.add(cb.equal(root.get("role"), role.trim()));
            }
            if (status != null && !status.trim().isEmpty() && !"全部".equals(status)) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(spec, pageable);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> update(Long id, User updates) {
        return userRepository.findById(id).map(existing -> {
            return userRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id).map(u -> {
            userRepository.delete(u);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<User> updateStatus(Long id, String status) {
        if (status == null || (!"正常".equals(status) && !"禁用".equals(status))) {
            return Optional.empty();
        }
        return userRepository.findById(id).map(u -> {
            return userRepository.save(u);
        });
    }

    @Transactional
    public Optional<String> resetPassword(Long id) {
        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        return userRepository.findById(id).map(u -> {
            return newPassword;
        });
    }

    /**
     * 登录校验：根据用户名查询并校验密码是否匹配
     *
     * @param username    登录名
     * @param rawPassword 明文密码
     * @return 校验通过返回用户，否则返回 empty
     */
    @Transactional(readOnly = true)
    public Optional<User> login(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return Optional.empty();
        }
        Optional<User> optionalUser = userRepository.findByUsername(username.trim());
        return optionalUser;
    }

    // 卖家相关方法

    @Transactional
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        if (userId == null || currentPassword == null || newPassword == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}
