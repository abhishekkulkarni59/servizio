package com.example.servizio.service.client;

import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.ReservationDTO;
import com.example.servizio.payload.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    Page<AdDTO> getAllAds(Pageable pageable);
    Page<AdDTO> searchAdByName(String name, Pageable pageable);

    String bookService(ReservationDTO reservationDTO);

    AdDetailsForClientDTO getAdDetailsByAdId(Long adId);
    List<ReservationDTO> getAllBookingsByUserId(long userId);

    Boolean giveReview(ReviewDTO reviewDTO);
}
