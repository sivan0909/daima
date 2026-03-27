package com.example.secondhand.repository;

import com.example.secondhand.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByStatusOrderBySortAsc(String status);

    Optional<Category> findByCode(String code);

    List<Category> findAllByOrderBySortAsc();
}
