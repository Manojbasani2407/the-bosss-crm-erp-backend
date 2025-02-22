package com.crm.erp.controller;

import com.crm.erp.model.Project;
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

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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
     * Create a new project. Automatically sets the onboard date to the current date.
     *
     * @param project Project details
     * @return Created project
     */
    @PostMapping("/")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        LOGGER.info("🆕 Creating new project: {}", project.getName());

        // Ensure onboardDate is set to the current date if not provided
        if (project != null && project.getOnboardDate() == null) {
            project.setOnboardDate(LocalDate.now());
        }

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
