package com.example.secondhand.init;

import com.example.secondhand.entity.Category;
import com.example.secondhand.entity.User;
import com.example.secondhand.repository.CategoryRepository;
import com.example.secondhand.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminUser();
        initCategories();
    }

    private void initAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("admin");
            userRepository.save(admin);
            System.out.println("Admin user created");
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            String[][] categories = {
                    {"数码产品", "digital"},
                    {"服饰鞋包", "clothing"},
                    {"运动户外", "sports"},
                    {"图书文具", "books"},
                    {"生活用品", "daily"},
                    {"其他", "other"}
            };
            for (String[] category : categories) {
                Category c = new Category();
                c.setName(category[0]);
                c.setCode(category[1]);
                categoryRepository.save(c);
            }
            System.out.println("Categories initialized");
        }
    }
}
