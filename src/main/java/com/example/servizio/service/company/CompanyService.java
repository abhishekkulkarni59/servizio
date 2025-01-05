package com.example.servizio.service.company;

import com.example.servizio.entity.Ad;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.ReservationDTO;

import java.io.IOException;
import java.util.List;

public interface CompanyService {

    Ad postAd(Long userId, AdDTO adDTO) throws IOException;
    List<AdDTO> getAllAds(long userId) ;
    AdDTO getAdById(long adId);
    boolean updateAd(long adId,AdDTO adDTO) throws IOException;
    boolean deleteAd(long adId);
    List<ReservationDTO> getAllAdBookings(long companyId);
    boolean changeBookingStatus(long bookingId,String status);

}
