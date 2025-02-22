package com.crm.erp.repository;

import com.crm.erp.model.Invoice;
import com.crm.erp.model.InvoiceStatus;
import com.crm.erp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Fetch all invoices by status (PAID, PENDING, OVERDUE)
     *
     * @param status Invoice status
     * @return List of invoices with the given status
     */
    List<Invoice> findByStatus(InvoiceStatus status);

    /**
     * Fetch all invoices associated with a specific client
     *
     * @param client The client entity
     * @return List of invoices for the given client
     */
    List<Invoice> findByClient(Client client);
}
