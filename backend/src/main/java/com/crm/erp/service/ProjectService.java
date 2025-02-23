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
     * Retrieves a project by its projectId along with client details.
     *
     * @param projectId Project ID
     * @return Project with client details
     * @throws ResourceNotFoundException if project is not found
     */
    public Project getProjectById(Long projectId) {
        LOGGER.info("Fetching project with projectId: {}", projectId);
        return projectRepository.findByIdWithClient(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with projectId: " + projectId));
    }

    /**
     * Updates an existing project with new details.
     *
     * @param projectId      Project ID
     * @param projectDetails Updated project details
     * @return Updated project
     * @throws ResourceNotFoundException if project is not found
     */
    @Transactional
    public Project updateProject(Long projectId, Project projectDetails) {
        LOGGER.info("Updating project with projectId: {}", projectId);

        Project existingProject = projectRepository.findByIdWithClient(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with projectId: " + projectId));

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

        LOGGER.info("Project with projectId {} successfully updated", projectId);
        return projectRepository.save(existingProject);
    }

    /**
     * Moves a project to the Deleted Projects table instead of permanently deleting it.
     *
     * @param projectId Project ID
     * @throws ResourceNotFoundException if project is not found
     */
    @Transactional
    public void deleteProject(Long projectId) {
        LOGGER.info("Attempting to delete project with projectId: {}", projectId);

        Project project = projectRepository.findByIdWithClient(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with projectId: " + projectId));

        // Move project details to DeletedProject, including all relevant fields.
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
        // Optionally, if you want to store the projectId in the deleted project record,
        // you could add a similar field to DeletedProject.

        deletedProjectRepository.save(deletedProject);
        projectRepository.deleteById(projectId);

        LOGGER.info("Project with projectId {} moved to deleted projects successfully", projectId);
    }

    /**
     * Restores a project from the Deleted Projects table back to active projects.
     *
     * @param projectId Deleted Project ID
     * @return Restored Project
     * @throws ResourceNotFoundException if deleted project is not found
     */
    @Transactional
    public Project restoreDeletedProject(Long projectId) {
        LOGGER.info("Attempting to restore deleted project with projectId: {}", projectId);

        DeletedProject deletedProject = deletedProjectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Deleted Project not found with projectId: " + projectId));

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

        deletedProjectRepository.deleteById(projectId);
        LOGGER.info("Deleted project with projectId {} restored successfully", projectId);
        return projectRepository.save(restoredProject);
    }
}
