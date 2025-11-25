package com.clinic.system.service.impl;

import com.clinic.system.model.Invoice;
import com.clinic.system.model.InvoiceItem;
import com.clinic.system.model.Patient;
import com.clinic.system.model.Receptionist;
import com.clinic.system.repository.InvoiceItemRepository;
import com.clinic.system.repository.InvoiceRepository;
import com.clinic.system.repository.PatientRepository;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository; // for Receptionist

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceItemRepository invoiceItemRepository,
                              PatientRepository patientRepository,
                              UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Invoice createInvoice(Long patientId, Long receptionistId, String status, BigDecimal totalAmount) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        Receptionist receptionist = null;
        if (receptionistId != null) {
            receptionist = (Receptionist) userRepository.findById(receptionistId)
                    .orElseThrow(() -> new RuntimeException("Receptionist not found with id: " + receptionistId));
        }

        Invoice invoice = new Invoice(patient, status, totalAmount);
        invoice.setIssuedBy(receptionist);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    @Override
    public List<Invoice> getInvoicesForPatient(Long patientId) {
        return invoiceRepository.findByPatientId(patientId);
    }

    @Transactional
    @Override
    public Invoice addItemToInvoice(Long invoiceId, InvoiceItem item) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.addItem(item);
        recalculateTotal(invoice);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    @Override
    public Invoice removeItemFromInvoice(Long invoiceId, Long itemId) {
        Invoice invoice = getInvoiceById(invoiceId);

        Iterator<InvoiceItem> iterator = invoice.getItems().iterator();
        while (iterator.hasNext()) {
            InvoiceItem it = iterator.next();
            if (it.getId().equals(itemId)) {
                iterator.remove();
                it.setInvoice(null);
                invoiceItemRepository.delete(it);
                break;
            }
        }

        recalculateTotal(invoice);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    @Override
    public Invoice updateStatus(Long invoiceId, String status) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        invoiceRepository.delete(invoice);
    }

    private void recalculateTotal(Invoice invoice) {
        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceItem item : invoice.getItems()) {
            BigDecimal line = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);
        }
        invoice.setTotalAmount(total);
    }
}
