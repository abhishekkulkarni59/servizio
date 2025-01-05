package com.example.servizio.service.client;

import com.example.servizio.entity.Ad;
import com.example.servizio.entity.Reservation;
import com.example.servizio.entity.Review;
import com.example.servizio.entity.User;
import com.example.servizio.enums.ReservationStatus;
import com.example.servizio.enums.ReviewStatus;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.AdDetailsForClientDTO;
import com.example.servizio.payload.ReservationDTO;
import com.example.servizio.payload.ReviewDTO;
import com.example.servizio.repository.AdRepository;
import com.example.servizio.repository.ReservationRepository;
import com.example.servizio.repository.ReviewRepository;
import com.example.servizio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public String bookService(ReservationDTO reservationDTO) {
        Optional<Ad> optionalAd = adRepository.findById(reservationDTO.getAdId());
        Optional<User> optionalUser = userRepository.findById(reservationDTO.getUserId());

        if (optionalAd.isPresent() && optionalUser.isPresent()) {
            boolean bookingExists = reservationRepository.existsByAd_IdAndBookDate(
                    reservationDTO.getAdId(),
                    reservationDTO.getBookDate()
            );

            if (bookingExists) {
                return "Booking Conflict";
            }
            Reservation reservation = new Reservation();

            reservation.setBookDate(reservationDTO.getBookDate());
            reservation.setReservationStatus(ReservationStatus.PENDING);
            reservation.setUser(optionalUser.get());

            reservation.setAd(optionalAd.get());
            reservation.setCompany(optionalAd.get().getUser());
            reservation.setReviewStatus(ReviewStatus.FALSE);

            reservationRepository.save(reservation);
            return "OK";
        }
        if(!optionalAd.isPresent()){
            return "Ad not Present";}
        else {
            return "User not Present";
        }
    }

    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        AdDetailsForClientDTO adDetailsForClientDTO = new AdDetailsForClientDTO();
        if (optionalAd.isPresent()) {
            adDetailsForClientDTO.setAdDTO(optionalAd.get().getAdDTO());

            List<Review> reviewList = reviewRepository.findAllByAdId(adId);
            adDetailsForClientDTO.setReviewDTOList(reviewList
                    .stream()
                    .map(Review::getDto)
                    .collect(Collectors.toList()));
        }
        return adDetailsForClientDTO;
    }

    @Override
    public List<ReservationDTO> getAllBookingsByUserId(long userId) {
        return reservationRepository.findAllByUserId(userId)
                .stream()
                .map(Reservation::getReservationDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean giveReview(ReviewDTO reviewDTO) {
        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());

        Optional<Reservation> optionalBooking = reservationRepository.findById(reviewDTO.getBookId());

        if (optionalUser.isPresent() && optionalBooking.isPresent()) {
            Review review = new Review();

            review.setReviewDate(new Date());
            review.setReview(reviewDTO.getReview());
            review.setRating(reviewDTO.getRating());

            review.setUser(optionalUser.get());
            review.setAd(optionalBooking.get().getAd());

            reviewRepository.save(review);

            Reservation booking = optionalBooking.get();
            booking.setReviewStatus(ReviewStatus.TRUE);

            reservationRepository.save(booking);

            return true;
        }
        return false;
    }
}
