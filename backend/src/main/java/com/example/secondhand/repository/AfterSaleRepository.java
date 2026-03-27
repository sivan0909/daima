package com.example.secondhand.repository;

import com.example.secondhand.entity.AfterSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfterSaleRepository extends JpaRepository<AfterSale, Long> {

    Page<AfterSale> findByStatus(String status, Pageable pageable);
}

