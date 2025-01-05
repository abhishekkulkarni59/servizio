package com.example.servizio.service.client;

import com.example.servizio.entity.Ad;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.ReviewDTO;
import com.example.servizio.repository.AdRepository;
import com.example.servizio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Page<AdDTO> getAllAds(Pageable pageable) {
        log.error("Entered get all ads");
        Page<Ad> adPage = adRepository.findAll(pageable);

        return adPage.map(Ad::getAdDTO);
    }

    @Override
    public Page<AdDTO> searchAdByName(String name, Pageable pageable) {
        Page<Ad> adPage = adRepository.findAllByServiceNameContaining(name, pageable);
        return adPage.map(Ad::getAdDTO);
    }

    @Override
    public String bookService(ReservationDTO reservationDTO) {
        return "";
    }

    @Override
    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId) {
        return null;
    }

    @Override
    public List<ReservationDTO> getAllBookingsByUserId(long userId) {
        return null;
    }

    @Override
    public Boolean giveReview(ReviewDTO reviewDTO) {
        return null;
    }
}
