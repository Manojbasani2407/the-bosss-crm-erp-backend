package com.crm.erp.repository;

import com.crm.erp.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Client entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Fetch all clients along with their associated projects.
     * This helps avoid LazyInitializationException and improves performance.
     */
    @EntityGraph(attributePaths = {"projects"})
    List<Client> findAll();

    /**
     * Fetch a client by ID along with all their associated projects.
     * Ensures the client’s projects are loaded eagerly.
     *
     * @param id Client ID
     * @return Optional Client with projects
     */
    @EntityGraph(attributePaths = {"projects"})
    Optional<Client> findById(Long id);
}
