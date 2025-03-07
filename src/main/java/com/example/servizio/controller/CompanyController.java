package com.example.servizio.controller;

import com.example.servizio.entity.Ad;
import com.example.servizio.enums.ApiErrorCode;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.ReservationDTO;
import com.example.servizio.service.company.CompanyService;
import com.example.servizio.util.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

//  Get All Ads By UserId
    @GetMapping("/ads/{userId}")
    public ResponseEntity<?> getAllAdsByUserId(@PathVariable long userId)
    {
        List<AdDTO> ads = companyService.getAllAds(userId);

        if (ads.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No ads found for the user with ID: " + userId);
        }

        return ResponseEntity.ok(companyService.getAllAds(userId));
    }

//  Create Ad
    @PostMapping("/ad/{userId}")
    public ResponseEntity<?> postAd(@PathVariable Long userId, @ModelAttribute AdDTO adDTO) throws IOException {
        try {
            Ad createdAd = companyService.postAd(userId, adDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.buildErrorResponse(ApiErrorCode.AD_POST_FAILED.getCode(),
                    ApiErrorCode.AD_POST_FAILED.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the ad.");
        }
    }

//  Get Ad By Id
    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAdById(@PathVariable long adId)
    {
        AdDTO adDTO= companyService.getAdById(adId);
        if(adDTO!=null)
        {
            return ResponseEntity.ok(adDTO);
        }
        else {
            return ResponseUtil.buildErrorResponse(ApiErrorCode.AD_NOT_FOUND.getCode(),
                    ApiErrorCode.AD_NOT_FOUND.getMessage(),HttpStatus.NOT_FOUND);

        }
    }

//  Update Ad
    @PutMapping("/ad/{adId}")
    public ResponseEntity<?> updateAd(@PathVariable Long adId,  @ModelAttribute AdDTO adDTO) throws IOException {
        boolean success = companyService.updateAd(adId, adDTO);
        if (success) {
            AdDTO updatedAd = companyService.getAdById(adId); // Fetch updated ad if necessary
            return ResponseEntity.status(HttpStatus.OK).body(updatedAd);
        }
        else {
            return ResponseUtil.buildErrorResponse(ApiErrorCode.AD_UPDATE_FAILED.getCode(),
                    ApiErrorCode.AD_UPDATE_FAILED.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

//  Delete Ad
    @DeleteMapping("/ad/{adId}")
    public ResponseEntity<?> deletedAd(@PathVariable Long adId){
        boolean success=companyService.deleteAd(adId);
        if(success){
            return ResponseEntity.status(HttpStatus.OK).body("Advertisement deleted successfully.");

        }
        else{
            return ResponseUtil.buildErrorResponse(ApiErrorCode.AD_DELETE_FAILED.getCode(),
                    ApiErrorCode.AD_DELETE_FAILED.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//  Get All Bookings By Company Id
    @GetMapping("/bookings/{companyId}")
    public  ResponseEntity<?> getAllAdBookings(@PathVariable long companyId)
    {
        try {
            List<ReservationDTO> bookings = companyService.getAllAdBookings(companyId);

            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bookings found for the company with ID: " + companyId);
            }
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the bookings.");
        }
    }

//  Change The Booking Status
    @GetMapping("/booking/{bookingId}/{status}")
    public  ResponseEntity<?> changeBookingStatus(@PathVariable long bookingId, @PathVariable String status, HttpServletResponse response) throws JSONException, IOException {
        boolean success= companyService.changeBookingStatus(bookingId,status);
        if(success)
        {
            response.getWriter().write(new JSONObject()
                    .put("message", "\t\n" +
                            "Booking status updated successfully.")
                    .toString());
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No bookings found for this Booking Id: " + bookingId);
        }
    }

}
