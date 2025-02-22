package com.crm.erp.controller;

import com.crm.erp.dto.ProjectDTO;
import com.crm.erp.model.Client;
import com.crm.erp.model.Project;
import com.crm.erp.service.ClientService;
import com.crm.erp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing project-related operations.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    private final ClientService clientService;

    public ProjectController(ProjectService projectService, ClientService clientService) {
        this.projectService = projectService;
        this.clientService = clientService;
    }

    /**
     * Fetch all active projects.
     *
     * @return List of active projects
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        LOGGER.info("📌 Fetching all active projects...");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Get a project by ID.
     *
     * @param projectId Project ID
     * @return Project details
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        LOGGER.info("🔍 Fetching project with ID: {}", projectId);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    /**
     * Create a new project.
     * Expects a payload with a clientId and deadline (among other required fields).
     * Automatically sets the onboard date to the current date if not provided.
     *
     * @param projectDTO Project details
     * @return Created project
     */
    @PostMapping("/add")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        LOGGER.info("🆕 Creating new project: {}", projectDTO.getName());

        // Validate required fields: name, productOwner, expectedDeliveryDate, deadline, clientId
        if (projectDTO.getName() == null || projectDTO.getName().isEmpty() ||
                projectDTO.getProductOwner() == null || projectDTO.getProductOwner().isEmpty() ||
                projectDTO.getExpectedDeliveryDate() == null ||
                projectDTO.getDeadline() == null ||
                projectDTO.getClientId() == null) {
            LOGGER.error("Missing required fields in project payload");
            return ResponseEntity.badRequest().build();
        }

        // Create a new Project entity from the DTO
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setProductOwner(projectDTO.getProductOwner());
        project.setExpectedDeliveryDate(projectDTO.getExpectedDeliveryDate());
        project.setDeadline(projectDTO.getDeadline());
        project.setBudget(projectDTO.getBudget());
        project.setAmountSpent(projectDTO.getAmountSpent());
        project.setStatus(projectDTO.getStatus());
        project.setLastUpdateComments(projectDTO.getLastUpdateComments());
        // Set onboardDate to current date if not provided
        project.setOnboardDate(projectDTO.getOnboardDate() != null ? projectDTO.getOnboardDate() : LocalDate.now());

        // Fetch the client entity using the provided clientId
        Client client = clientService.getClientById(projectDTO.getClientId());
        if (client == null) {
            LOGGER.error("Client not found for clientId: {}", projectDTO.getClientId());
            return ResponseEntity.badRequest().build();
        }
        project.setClient(client);

        Project createdProject = projectService.addProject(project);
        return ResponseEntity.status(201).body(createdProject);
    }

    /**
     * Update an existing project.
     *
     * @param projectId Project ID
     * @param updatedProject Updated project details
     * @return Updated project
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project updatedProject) {
        LOGGER.info("✏️ Updating project with ID: {}", projectId);
        return ResponseEntity.ok(projectService.updateProject(projectId, updatedProject));
    }

    /**
     * Soft delete a project by moving it to the Deleted Projects table.
     *
     * @param projectId Project ID
     * @return No content response
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        LOGGER.info("🗑️ Soft deleting project with ID: {}", projectId);
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore a project from the Deleted Projects table.
     *
     * @param projectId Deleted Project ID
     * @return Restored project
     */
    @PostMapping("/restore/{projectId}")
    public ResponseEntity<Project> restoreDeletedProject(@PathVariable Long projectId) {
        LOGGER.info("♻️ Restoring deleted project with ID: {}", projectId);
        return ResponseEntity.ok(projectService.restoreDeletedProject(projectId));
    }
}
