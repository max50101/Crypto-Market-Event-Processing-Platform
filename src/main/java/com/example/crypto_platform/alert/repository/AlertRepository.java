package com.example.crypto_platform.alert.repository;

import com.example.crypto_platform.alert.entity.Alert;
import com.example.crypto_platform.alert.entity.AlertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AlertRepository  extends JpaRepository<Alert,Long> {
    List<Alert> findBySymbolAndStatus(String symbol, AlertStatus status);

    Page<Alert> findBySymbolAndStatus(String sybmol, AlertStatus status, Pageable pageable);
    Page<Alert> findBySymbol(String  symbol, Pageable pageable);
    Page<Alert> findByStatus(AlertStatus status,Pageable pageable);

    List<Alert> findByStatus(AlertStatus status);

}
