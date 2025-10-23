package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.entity.TransactionLog;
import com.StockInventory.InventoryManagement.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class TransactionLogController {

    private final TransactionLogRepository transactionLogRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionLog>> getAllLogs() {
        return ResponseEntity.ok(transactionLogRepository.findAll());
    }
}
