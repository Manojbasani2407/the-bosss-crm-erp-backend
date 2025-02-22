package com.crm.erp.service;

import com.crm.erp.dto.InvoiceDTO;
import com.crm.erp.exception.ResourceNotFoundException;
import com.crm.erp.model.Client;
import com.crm.erp.model.Invoice;
import com.crm.erp.model.InvoiceStatus;
import com.crm.erp.repository.InvoiceRepository;
import com.crm.erp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ClientRepository clientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Creates a new invoice with required fields populated.
     */
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Client client = clientRepository.findById(invoiceDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber() != null ? invoiceDTO.getInvoiceNumber() : generateInvoiceNumber());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setIssueDate(invoiceDTO.getIssueDate() != null ? invoiceDTO.getIssueDate() : LocalDate.now());
        invoice.setDueDate(invoiceDTO.getDueDate() != null ? invoiceDTO.getDueDate() : LocalDate.now().plusDays(7));
        invoice.setStatus(invoiceDTO.getStatus() != null ? invoiceDTO.getStatus() : InvoiceStatus.PENDING);
        invoice.setClient(client);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToDTO(savedInvoice);
    }

    /**
     * Retrieves all invoices.
     */
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an invoice by its ID.
     */
    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    /**
     * Retrieves invoices filtered by status.
     */
    public List<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing invoice with provided values.
     */
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (invoiceDTO.getInvoiceNumber() != null) invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        if (invoiceDTO.getAmount() != null) invoice.setAmount(invoiceDTO.getAmount());
        if (invoiceDTO.getIssueDate() != null) invoice.setIssueDate(invoiceDTO.getIssueDate());
        if (invoiceDTO.getDueDate() != null) invoice.setDueDate(invoiceDTO.getDueDate());
        if (invoiceDTO.getStatus() != null) invoice.setStatus(invoiceDTO.getStatus());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToDTO(updatedInvoice);
    }

    /**
     * Deletes an invoice by its ID.
     */
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        invoiceRepository.delete(invoice);
    }

    /**
     * Auto-generates an invoice number following the format: INV-XXXX.
     */
    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return "INV-" + String.format("%04d", count);
    }

    /**
     * Updates overdue invoices by checking due dates.
     * This method can be scheduled to run daily.
     */
    public void updateOverdueInvoices() {
        List<Invoice> overdueInvoices = invoiceRepository.findByStatus(InvoiceStatus.PENDING)
                .stream()
                .filter(invoice -> invoice.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        overdueInvoices.forEach(invoice -> invoice.setStatus(InvoiceStatus.OVERDUE));
        invoiceRepository.saveAll(overdueInvoices);
    }

    /**
     * Maps an Invoice entity to an InvoiceDTO.
     */
    private InvoiceDTO mapToDTO(Invoice invoice) {
        return new InvoiceDTO(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getAmount(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getStatus(),
                invoice.getClient().getId()
        );
    }
}
