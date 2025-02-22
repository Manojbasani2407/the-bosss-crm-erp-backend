package com.crm.erp.service;

import com.crm.erp.exception.ResourceNotFoundException;
import com.crm.erp.model.Project;
import com.crm.erp.model.DeletedProject;
import com.crm.erp.repository.ProjectRepository;
import com.crm.erp.repository.DeletedProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing project operations.
 */
@Service
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final DeletedProjectRepository deletedProjectRepository;

    public ProjectService(ProjectRepository projectRepository, DeletedProjectRepository deletedProjectRepository) {
        this.projectRepository = projectRepository;
        this.deletedProjectRepository = deletedProjectRepository;
    }

    /**
     * Creates a new project, ensuring it is associated with a client.
     *
     * @param project The project to be created
     * @return The saved project
     */
    @Transactional
    public Project addProject(Project project) {
        if (project.getClient() == null) {
            throw new IllegalArgumentException("Project must be associated with a client.");
        }
        LOGGER.info("Adding new project: {}", project.getName());
        return projectRepository.save(project);
    }

    /**
     * Retrieves all projects with their associated client details.
     *
     * @return List of all projects
     */
    public List<Project> getAllProjects() {
        LOGGER.info("Fetching all projects with client details");
        return projectRepository.findAll();
    }

    /**
     * Retrieves a project by ID along with client details.
     *
     * @param id Project ID
     * @return Project with client details
     * @throws ResourceNotFoundException if project is not found
     */
    public Project getProjectById(Long id) {
        LOGGER.info("Fetching project with ID: {}", id);
        return projectRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
    }

    /**
     * Updates an existing project with new details.
     *
     * @param id             Project ID
     * @param projectDetails Updated project details
     * @return Updated project
     * @throws ResourceNotFoundException if project is not found
     */
    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        LOGGER.info("Updating project with ID: {}", id);

        Project existingProject = projectRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        existingProject.setName(projectDetails.getName());
        existingProject.setDeadline(projectDetails.getDeadline());
        existingProject.setStatus(projectDetails.getStatus());
        existingProject.setProductOwner(projectDetails.getProductOwner());
        existingProject.setExpectedDeliveryDate(projectDetails.getExpectedDeliveryDate());
        existingProject.setBudget(projectDetails.getBudget());
        existingProject.setAmountSpent(projectDetails.getAmountSpent());
        existingProject.setLastUpdateComments(projectDetails.getLastUpdateComments());

        if (projectDetails.getClient() != null) {
            existingProject.setClient(projectDetails.getClient());
        }

        LOGGER.info("Project with ID {} successfully updated", id);
        return projectRepository.save(existingProject);
    }

    /**
     * Moves a project to the Deleted Projects table instead of permanently deleting it.
     *
     * @param id Project ID
     * @throws ResourceNotFoundException if project is not found
     */
    @Transactional
    public void deleteProject(Long id) {
        LOGGER.info("Attempting to delete project with ID: {}", id);

        Project project = projectRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        // Move project details to DeletedProject
        DeletedProject deletedProject = new DeletedProject();
        deletedProject.setName(project.getName());
        deletedProject.setDeadline(project.getDeadline());
        deletedProject.setStatus(project.getStatus());
        deletedProject.setProductOwner(project.getProductOwner());
        deletedProject.setExpectedDeliveryDate(project.getExpectedDeliveryDate());
        deletedProject.setBudget(project.getBudget());
        deletedProject.setAmountSpent(project.getAmountSpent());
        deletedProject.setLastUpdateComments(project.getLastUpdateComments());
        deletedProject.setClient(project.getClient());

        deletedProjectRepository.save(deletedProject);
        projectRepository.deleteById(id);

        LOGGER.info("Project with ID {} moved to deleted projects successfully", id);
    }

    /**
     * Restores a project from the Deleted Projects table back to active projects.
     *
     * @param id Deleted Project ID
     * @return Restored Project
     * @throws ResourceNotFoundException if deleted project is not found
     */
    @Transactional
    public Project restoreDeletedProject(Long id) {
        LOGGER.info("Attempting to restore deleted project with ID: {}", id);

        DeletedProject deletedProject = deletedProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deleted Project not found with ID: " + id));

        Project restoredProject = new Project();
        restoredProject.setName(deletedProject.getName());
        restoredProject.setDeadline(deletedProject.getDeadline());
        restoredProject.setStatus(deletedProject.getStatus());
        restoredProject.setProductOwner(deletedProject.getProductOwner());
        restoredProject.setExpectedDeliveryDate(deletedProject.getExpectedDeliveryDate());
        restoredProject.setBudget(deletedProject.getBudget());
        restoredProject.setAmountSpent(deletedProject.getAmountSpent());
        restoredProject.setLastUpdateComments(deletedProject.getLastUpdateComments());
        restoredProject.setClient(deletedProject.getClient());

        deletedProjectRepository.deleteById(id);
        LOGGER.info("Deleted project with ID {} restored successfully", id);
        return projectRepository.save(restoredProject);
    }
}
