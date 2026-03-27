package com.example.secondhand.repository;

import com.example.secondhand.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e join e.order o join o.items i where i.productId = :productId order by e.createTime desc")
    List<Evaluation> findByProductId(@Param("productId") Long productId);
}

