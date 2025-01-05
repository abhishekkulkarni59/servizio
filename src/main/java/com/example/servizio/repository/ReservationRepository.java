package com.example.servizio.repository;

import com.example.servizio.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByCompanyId(long companyId);
    List<Reservation> findAllByUserId(long userId);
    boolean existsByAd_IdAndBookDate(Long adId, Date bookDate);
}
