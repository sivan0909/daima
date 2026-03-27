package com.example.secondhand.repository;

import com.example.secondhand.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByTitleContaining(String title, Pageable pageable);

    List<Notice> findByStatusOrderByIsTopDescCreateTimeDesc(String status);
}
