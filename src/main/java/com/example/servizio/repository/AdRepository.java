package com.example.servizio.repository;

import com.example.servizio.entity.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByUserId(long userId);
    Page<Ad> findAllByServiceNameContaining(String name, Pageable pageable);
}
