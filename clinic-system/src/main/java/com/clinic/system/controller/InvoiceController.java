package com.clinic.system.controller;

import com.clinic.system.model.Invoice;
import com.clinic.system.model.InvoiceItem;
import com.clinic.system.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Create invoice (empty, or with initial total)
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
            @RequestParam Long patientId,
            @RequestParam(required = false) Long receptionistId,
            @RequestParam(defaultValue = "UNPAID") String status,
            @RequestParam(required = false) BigDecimal totalAmount
    ) {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        return ResponseEntity.ok(invoiceService.createInvoice(patientId, receptionistId, status, totalAmount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<Invoice>> getInvoicesForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(invoiceService.getInvoicesForPatient(patientId));
    }

    @PostMapping("/{invoiceId}/items")
    public ResponseEntity<Invoice> addItem(
            @PathVariable Long invoiceId,
            @RequestBody InvoiceItem item
    ) {
        return ResponseEntity.ok(invoiceService.addItemToInvoice(invoiceId, item));
    }

    @DeleteMapping("/{invoiceId}/items/{itemId}")
    public ResponseEntity<Invoice> removeItem(
            @PathVariable Long invoiceId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(invoiceService.removeItemFromInvoice(invoiceId, itemId));
    }

    @PutMapping("/{invoiceId}/status")
    public ResponseEntity<Invoice> updateStatus(
            @PathVariable Long invoiceId,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(invoiceService.updateStatus(invoiceId, status));
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }
}
