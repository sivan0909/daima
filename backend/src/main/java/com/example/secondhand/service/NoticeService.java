package com.example.secondhand.service;

import com.example.secondhand.entity.Notice;
import com.example.secondhand.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<Notice> findPage(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return noticeRepository.findByTitleContaining(keyword.trim(), pageable);
        }
        return noticeRepository.findAll(pageable);
    }

    public Optional<Notice> findById(Long id) {
        return noticeRepository.findById(id);
    }

    @Transactional
    public Notice create(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Transactional
    public Optional<Notice> update(Long id, Notice updates) {
        return noticeRepository.findById(id).map(existing -> {
            return noticeRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return noticeRepository.findById(id).map(n -> {
            noticeRepository.delete(n);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<Notice> updateTop(Long id, boolean isTop) {
        return noticeRepository.findById(id).map(n -> {
            return noticeRepository.save(n);
        });
    }

    @Transactional
    public Optional<Notice> updateStatus(Long id, String status) {
        if (status == null || (!"已发布".equals(status) && !"草稿".equals(status))) {
            return Optional.empty();
        }
        return noticeRepository.findById(id).map(n -> {
            return noticeRepository.save(n);
        });
    }
}
