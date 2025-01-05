package com.example.servizio.service.company;

import com.example.servizio.entity.Ad;
import com.example.servizio.entity.User;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.payload.ReservationDTO;
import com.example.servizio.repository.AdRepository;
import com.example.servizio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    public Ad postAd(Long userId, AdDTO adDTO) throws IOException {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent() &&
                adDTO.getImg() != null && !adDTO.getImg().isEmpty() &&
                adDTO.getPrice() != null && adDTO.getPrice() > 0) {
            Ad ad = new Ad();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setImg(adDTO.getImg().getBytes());
            ad.setPrice(adDTO.getPrice());
            ad.setUser(optionalUser.get());

            return adRepository.save(ad);
        }
        throw new IllegalArgumentException("Invalid ad data or user not found.");
    }

    @Override
    public List<AdDTO> getAllAds(long userId) {
        return adRepository.findAllByUserId(userId)
                .stream()
                .map(Ad::getAdDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdDTO getAdById(long adId)
    {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if(optionalAd.isPresent())
        {
            return optionalAd.get().getAdDTO();
        }
        return null;
    }

    @Override
    public boolean updateAd(long adId, AdDTO adDTO) throws IOException {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if(optionalAd.isPresent()
                && adDTO.getImg() != null && !adDTO.getImg().isEmpty()&&
                adDTO.getPrice() != null && adDTO.getPrice() > 0)
        {
            Ad ad =optionalAd.get();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setPrice(adDTO.getPrice());

            if(adDTO.getImg()!=null)
            {
                ad.setImg(adDTO.getImg().getBytes());
            }
            adRepository.save(ad);

            return true;
        }
        return false;
    }

    public boolean deleteAd(long adId){
        Optional<Ad> optionalAd=adRepository.findById(adId);
        if(optionalAd.isPresent()){
            adRepository.delete(optionalAd.get());
            return true;
        }
        return false;
    }

    @Override
    public List<ReservationDTO> getAllAdBookings(long companyId) {
        return List.of();
    }

    @Override
    public boolean changeBookingStatus(long bookingId, String status) {
        return false;
    }

}
