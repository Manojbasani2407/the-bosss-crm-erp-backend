package com.crm.erp.repository;

import com.crm.erp.model.DeletedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Deleted Projects.
 */
@Repository
public interface DeletedProjectRepository extends JpaRepository<DeletedProject, Long> {
}
