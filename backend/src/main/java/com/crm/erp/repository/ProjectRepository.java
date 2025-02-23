package com.crm.erp.repository;

import com.crm.erp.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for handling Project-related database operations.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Retrieves all projects along with their associated client details.
     * Uses `@EntityGraph` to eagerly fetch client details to avoid LazyInitializationException.
     *
     * @return List of projects with client details
     */
    @EntityGraph(attributePaths = {"client"})
    List<Project> findAll();

    /**
     * Retrieves a project by projectId, ensuring associated client details are also fetched.
     * Uses a JPQL `JOIN FETCH` query for better performance.
     *
     * @param id Project ID
     * @return Optional containing the project and its associated client
     */
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.client WHERE p.projectId = :id")
    Optional<Project> findByIdWithClient(@Param("id") Long id);

    /**
     * Retrieves projects by status with client details eagerly loaded.
     *
     * @param status Project status
     * @return List of projects with the given status
     */
    @EntityGraph(attributePaths = {"client"})
    List<Project> findByStatus(String status);

    /**
     * Retrieves projects within a budget range, ensuring client data is included.
     *
     * @param minBudget Minimum budget value
     * @param maxBudget Maximum budget value
     * @return List of projects within the specified budget range
     */
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.client WHERE p.budget BETWEEN :minBudget AND :maxBudget")
    List<Project> findProjectsByBudgetRange(@Param("minBudget") Double minBudget, @Param("maxBudget") Double maxBudget);

    /**
     * Retrieves all projects along with their clients sorted by expected delivery date.
     *
     * @return List of projects sorted by expected delivery date
     */
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.client ORDER BY p.expectedDeliveryDate ASC")
    List<Project> findAllSortedByDeliveryDate();
}
