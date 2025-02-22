package com.crm.erp.repository;

import com.crm.erp.model.Invoice;
import com.crm.erp.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
}
