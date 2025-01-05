package com.example.servizio.controller;

import com.example.servizio.enums.ApiErrorCode;
import com.example.servizio.exception.ConflictException;
import com.example.servizio.exception.ResourceNotFoundException;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.AdDetailsForClientDTO;
import com.example.servizio.payload.ReservationDTO;
import com.example.servizio.payload.ReviewDTO;
import com.example.servizio.service.client.ClientService;
import com.example.servizio.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/ads")
    public ResponseEntity<Page<AdDTO>> getAllAds(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            Page<AdDTO> ads = clientService.getAllAds(pageable);
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching ads.");
        }
    }

    @GetMapping("/ads/search/{name}")
    public ResponseEntity<Page<AdDTO>> searchAdByService(
            @PathVariable String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AdDTO> ads = clientService.searchAdByName(name, pageable);
        if (ads.isEmpty()) {
            throw new ResourceNotFoundException("No ads found for service name: " + name);
        }
        return ResponseEntity.ok(ads);
    }

    @PostMapping("/book-service")
    public ResponseEntity<?> bookService(@RequestBody ReservationDTO reservationDTO) {
        try {
            String result = clientService.bookService(reservationDTO);

            switch (result) {
                case "OK":
                    return ResponseUtil.buildErrorResponse(ApiErrorCode.CLIENT_BOOKING_SUCCESSFUL.getCode(),
                            ApiErrorCode.CLIENT_BOOKING_SUCCESSFUL.getMessage(), HttpStatus.OK);
                case "Ad not Present":
                    return ResponseUtil.buildErrorResponse(ApiErrorCode.AD_NOT_FOUND.getCode(),
                            ApiErrorCode.AD_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
                case "User not Present":
                    return ResponseUtil.buildErrorResponse(ApiErrorCode.CLIENT_DOES_NOT_EXIST.getCode(),
                            ApiErrorCode.CLIENT_DOES_NOT_EXIST.getMessage(), HttpStatus.NOT_FOUND);
                case "Booking Conflict":
                    return ResponseUtil.buildErrorResponse(ApiErrorCode.BOOKING_EXIST.getCode(),
                            ApiErrorCode.BOOKING_EXIST.getMessage(), HttpStatus.CONFLICT);
                default:
                    return ResponseUtil.buildErrorResponse(ApiErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                            ApiErrorCode.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAdDetailsByAdId(@PathVariable Long adId) {
        AdDetailsForClientDTO adDetails = clientService.getAdDetailsByAdId(adId);
        if (adDetails == null) {
            throw new ResourceNotFoundException("Ad with ID " + adId + " not found.");
        }
        return ResponseEntity.ok(adDetails);
    }

    @GetMapping("/my-bookings/{userId}")
    public ResponseEntity<?> getAllBookingsByUserId(@PathVariable long userId) {
        List<ReservationDTO> bookings = clientService.getAllBookingsByUserId(userId);
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for user ID: " + userId);
        }
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("Review submission request by userId: " + reviewDTO.getUserId());
        boolean success = clientService.giveReview(reviewDTO);
        if (!success) {
            throw new ConflictException("Review submission failed. Review may already exist.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
