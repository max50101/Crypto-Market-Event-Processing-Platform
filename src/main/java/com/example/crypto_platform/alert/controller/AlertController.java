package com.example.crypto_platform.alert.controller;

import com.example.crypto_platform.alert.entity.Alert;
import com.example.crypto_platform.alert.entity.AlertStatus;
import com.example.crypto_platform.alert.repository.AlertRepository;

import com.example.crypto_platform.alert.service.AlertService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService service;
    public AlertController(AlertService service){

        this.service=service;
    }

    @GetMapping()
    public Page<Alert> getAlerts(@RequestParam(required = false)AlertStatus status,
                                 @RequestParam(required = false) String symbol,
                                 Pageable pageable){
        return service.getAlerts(status,symbol,pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlert(@PathVariable Long id){
        Alert alert= service.getAlertById(id);
        return ResponseEntity.ok(alert);
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Alert> postAlert (@RequestBody(required = true) Alert alert){
        Alert saved=service.createAlert(alert);
        URI location= URI.create("/api/alerts/"+saved.getId());
        return ResponseEntity
                .created(location)
                .body(saved);
    }





}
