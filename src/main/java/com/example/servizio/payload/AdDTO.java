package com.example.servizio.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdDTO {
    private Long id;

    private String serviceName;

    private String description;

    private Double price;

    private MultipartFile img;

    private byte[] returnedImg;

    private Long userId;

    private String companyName;
}
