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
     * Creates a new invoice and saves it in the database.
     */
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Client client = clientRepository.findById(invoiceDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setStatus(invoiceDTO.getStatus());
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
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return mapToDTO(invoice);
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
     * Updates an existing invoice.
     */
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setStatus(invoiceDTO.getStatus());

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
