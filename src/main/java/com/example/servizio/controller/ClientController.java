package com.example.servizio.controller;

import com.example.servizio.exception.ResourceNotFoundException;
import com.example.servizio.payload.AdDTO;
import com.example.servizio.service.client.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
