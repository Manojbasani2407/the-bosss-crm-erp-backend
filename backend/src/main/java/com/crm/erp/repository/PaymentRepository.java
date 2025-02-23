package com.crm.erp.repository;

import com.crm.erp.model.payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<payment, Long> {
    List<payment> findByClientId(Long clientId);
}
