package com.example.servizio.service.company;

import com.example.servizio.entity.Ad;
import com.example.servizio.entity.User;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.repository.AdRepository;
import com.example.servizio.repository.ReservationRepository;
import com.example.servizio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void postAd_ShouldSaveAd_WhenValidInput() throws IOException {

        Long userId = 1L;
        MultipartFile mockFile = mock(MultipartFile.class);
        Mockito.when(mockFile.getBytes()).thenReturn("imageData".getBytes()); // Mock file content

        AdDTO adDTO = new AdDTO();
        adDTO.setServiceName("Cleaning Service");
        adDTO.setDescription("Professional cleaning service for your home.");
        adDTO.setImg(mockFile);
        adDTO.setPrice(100.0);
        adDTO.setUserId(userId);
        adDTO.setCompanyName("CleanCo");

        User user = new User();
        user.setId(userId);

        Ad savedAd = new Ad();
        savedAd.setId(1L);
        savedAd.setServiceName(adDTO.getServiceName());
        savedAd.setDescription(adDTO.getDescription());
        savedAd.setPrice(adDTO.getPrice());
        savedAd.setImg(mockFile.getBytes());
        savedAd.setUser(user);

        // Mock behavior
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(adRepository.save(Mockito.any(Ad.class))).thenReturn(savedAd);

        // Act
        Ad result = companyService.postAd(userId, adDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Cleaning Service", result.getServiceName());
        assertEquals(100.0, result.getPrice());
        assertEquals(user, result.getUser());

    }

}