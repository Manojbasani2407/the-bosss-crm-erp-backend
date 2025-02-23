package com.crm.erp.service;

import com.crm.erp.dto.InvoiceDTO;
import com.crm.erp.exception.ResourceNotFoundException;
import com.crm.erp.model.Client;
import com.crm.erp.model.Invoice;
import com.crm.erp.model.InvoiceStatus;
import com.crm.erp.model.Project;
import com.crm.erp.repository.InvoiceRepository;
import com.crm.erp.repository.ClientRepository;
import com.crm.erp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ClientRepository clientRepository, ProjectRepository projectRepository) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
        this.projectRepository = projectRepository;
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Client client = clientRepository.findById(invoiceDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Project project = projectRepository.findById(invoiceDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceDTO.getInvoiceNumber() != null ? invoiceDTO.getInvoiceNumber() : generateInvoiceNumber())
                .amount(invoiceDTO.getAmount())
                .issueDate(invoiceDTO.getIssueDate() != null ? invoiceDTO.getIssueDate() : LocalDate.now())
                .dueDate(invoiceDTO.getDueDate() != null ? invoiceDTO.getDueDate() : LocalDate.now().plusDays(7))
                .status(invoiceDTO.getStatus() != null ? invoiceDTO.getStatus() : InvoiceStatus.PENDING)
                .client(client)
                .project(project)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToDTO(savedInvoice);
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    public List<InvoiceDTO> getInvoicesByProject(Long projectId) {
        return invoiceRepository.findByProject_ProjectId(projectId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (invoiceDTO.getInvoiceNumber() != null)
            invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        if (invoiceDTO.getAmount() != null)
            invoice.setAmount(invoiceDTO.getAmount());
        if (invoiceDTO.getIssueDate() != null)
            invoice.setIssueDate(invoiceDTO.getIssueDate());
        if (invoiceDTO.getDueDate() != null)
            invoice.setDueDate(invoiceDTO.getDueDate());
        if (invoiceDTO.getStatus() != null)
            invoice.setStatus(invoiceDTO.getStatus());

        if (invoiceDTO.getProjectId() != null) {
            Project project = projectRepository.findById(invoiceDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            invoice.setProject(project);
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToDTO(updatedInvoice);
    }

    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        invoiceRepository.delete(invoice);
    }

    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return "INV-" + String.format("%04d", count);
    }

    private InvoiceDTO mapToDTO(Invoice invoice) {
        return InvoiceDTO.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .amount(invoice.getAmount())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .status(invoice.getStatus())
                .clientId(invoice.getClient().getId())
                .projectId(invoice.getProject() != null ? invoice.getProject().getProjectId() : null)
                .build();
    }
}
