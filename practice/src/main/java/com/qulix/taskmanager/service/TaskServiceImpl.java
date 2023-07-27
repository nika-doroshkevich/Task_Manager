package com.qulix.taskmanager.service;

import com.qulix.taskmanager.dto.TaskDto;
import com.qulix.taskmanager.exception.EntityValidationException;
import com.qulix.taskmanager.exception.RestRequestException;
import com.qulix.taskmanager.mapper.EmployeeMapper;
import com.qulix.taskmanager.mapper.TaskMapper;
import com.qulix.taskmanager.model.Employee;
import com.qulix.taskmanager.model.Project;
import com.qulix.taskmanager.model.Task;
import com.qulix.taskmanager.repository.EmployeeRepository;
import com.qulix.taskmanager.repository.ProjectRepository;
import com.qulix.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.qulix.taskmanager.enums.TaskStatus.DELETED;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    public final TaskRepository taskRepository;
    public final ProjectRepository projectRepository;
    public final EmployeeRepository employeeRepository;

    @Override
    public List<TaskDto> getAllNotDeletedTasks() {
        List<Task> tasks = taskRepository.findByStatusIsNot(DELETED);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : tasks) {
            TaskDto newTaskDto = new TaskDto();
            TaskMapper.updateTaskDto(task, newTaskDto);
            newTaskDto.setTaskPerformers(EmployeeMapper.toDtoList(task.getEmployees()));
            taskDtoList.add(newTaskDto);
        }
        return taskDtoList;
    }

    @Override
    public TaskDto addTask(TaskDto taskDto, Integer projectId) {
        validate(taskDto);

        List<Employee> employees = EmployeeMapper.toEntityList(taskDto.getTaskPerformers());
        Task task = TaskMapper.toEntity(taskDto);
        task.setEmployees(employees);

        Project project = getProjectById(projectId);
        List<Task> projectTasks = project.getTasks();
        projectTasks.add(task);
        project.setTasks(projectTasks);
        projectRepository.save(project);

        task.setProject(project);
        Task savedTask = taskRepository.save(task);

        for (Employee employee : employees) {
            Employee foundEmployee = getEmployeeById(employee.getId());
            List<Task> employeeTasks = foundEmployee.getTasks();
            employeeTasks.add(savedTask);
            foundEmployee.setTasks(employeeTasks);
            employeeRepository.save(foundEmployee);
        }

        TaskDto newTaskDto = TaskMapper.toDto(savedTask);
        newTaskDto.setTaskPerformers(EmployeeMapper.toDtoList(savedTask.getEmployees()));

        return newTaskDto;
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        validateForDeletion(taskDto.getId());
        validate(taskDto);

        Task foundTask = getTaskById(taskDto.getId());
        TaskMapper.updateTask(taskDto, foundTask);
        List<Employee> newEmployeeList = EmployeeMapper.toEntityList(taskDto.getTaskPerformers());
        List<Employee> oldEmployeeList = foundTask.getEmployees();
        foundTask.setEmployees(newEmployeeList);

        updateTaskListForEmployees(oldEmployeeList, newEmployeeList, foundTask);

        Task task = taskRepository.save(foundTask);
        TaskDto newTaskDto = new TaskDto();
        TaskMapper.updateTaskDto(task, newTaskDto);
        newTaskDto.setTaskPerformers(EmployeeMapper.toDtoList(task.getEmployees()));
        return newTaskDto;
    }

    @Override
    public void deleteTask(Integer id) {
        validateForDeletion(id);

        Task task = getTaskById(id);
        task.setStatus(DELETED);

        List<Task> removeCandidates = new ArrayList<>();
        for (Employee employee : task.getEmployees()) {
            for (Task assignedTask : employee.getTasks()) {
                if (Objects.equals(assignedTask.getId(), id)) {
                    removeCandidates.add(assignedTask);
                }
            }
            employee.getTasks().removeAll(removeCandidates);
        }

        task.getEmployees().clear();

        taskRepository.save(task);
    }

    private Project getProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RestRequestException("Project with id " + id + " not found"));
    }

    private Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RestRequestException("Task with id " + id + " not found"));
    }

    private Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RestRequestException("Employee with id " + id + " not found"));
    }

    private void updateTaskListForEmployees(List<Employee> oldEmployeeList, List<Employee> newEmployeeList, Task task) {
        Set<Integer> oldEmployeeIdSet = new HashSet<>();
        Set<Integer> newEmployeeIdSet = new HashSet<>();
        for (Employee oldEmployee : oldEmployeeList) {
            oldEmployeeIdSet.add(oldEmployee.getId());
        }
        for (Employee newEmployee : newEmployeeList) {
            newEmployeeIdSet.add(newEmployee.getId());
        }

        if (oldEmployeeIdSet.equals(newEmployeeIdSet)) {
            return;
        }

        Set<Integer> removeCandidates = new HashSet<>();
        for (Integer employeeId : oldEmployeeIdSet) {
            if (newEmployeeIdSet.contains(employeeId)) {
                removeCandidates.add(employeeId);
            }
        }
        newEmployeeIdSet.removeAll(removeCandidates);
        oldEmployeeIdSet.removeAll(removeCandidates);

        for (Integer employeeId : oldEmployeeIdSet) {
            Employee foundEmployeeOld = getEmployeeById(employeeId);
            List<Task> employeeOldTaskList = foundEmployeeOld.getTasks();
            employeeOldTaskList.remove(task);
            foundEmployeeOld.setTasks(employeeOldTaskList);
            employeeRepository.save(foundEmployeeOld);
        }

        for (Integer employeeId : newEmployeeIdSet) {
            Employee foundEmployeeNew = getEmployeeById(employeeId);
            List<Task> employeeNewTaskList = foundEmployeeNew.getTasks();
            employeeNewTaskList.add(task);
            foundEmployeeNew.setTasks(employeeNewTaskList);
            employeeRepository.save(foundEmployeeNew);
        }
    }

    private void validate(TaskDto taskDto) {
        int hours = taskDto.getHours();
        LocalDate startDate = taskDto.getStartDate();
        LocalDate finishDate = taskDto.getFinishDate();
        if (hours <= 0) {
            throw new EntityValidationException("Hours should be a positive number");
        }
        if (startDate.isAfter(finishDate)) {
            throw new EntityValidationException("Finish date can't be earlier then start date");
        }
    }

    private void validateForDeletion(Integer id) {
        Task task = getTaskById(id);
        if (task.getStatus() == DELETED) {
            throw new EntityValidationException("Task has deleted");
        }
    }
}
