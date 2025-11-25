package com.clinic.system.service;

import com.clinic.system.model.Invoice;
import com.clinic.system.model.InvoiceItem;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(Long patientId, Long receptionistId, String status, BigDecimal totalAmount);

    Invoice getInvoiceById(Long id);

    List<Invoice> getInvoicesForPatient(Long patientId);

    Invoice addItemToInvoice(Long invoiceId, InvoiceItem item);

    Invoice removeItemFromInvoice(Long invoiceId, Long itemId);

    Invoice updateStatus(Long invoiceId, String status);

    void deleteInvoice(Long id);
}
