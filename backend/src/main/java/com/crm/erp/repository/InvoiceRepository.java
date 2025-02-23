package com.crm.erp.repository;

import com.crm.erp.model.Invoice;
import com.crm.erp.model.InvoiceStatus;
import com.crm.erp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByClient(Client client);

    // Updated method: traverse the project association and look for projectId.
    List<Invoice> findByProject_ProjectId(Long projectId);
}
