package com.qulix.taskmanager.repository;

import com.qulix.taskmanager.enums.ProjectStatus;
import com.qulix.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByStatusIsNot(ProjectStatus projectStatus);

    Project findByNameAndStatusIsNot(String name, ProjectStatus projectStatus);

    Project findByShortNameAndStatusIsNot(String shortName, ProjectStatus projectStatus);
}
