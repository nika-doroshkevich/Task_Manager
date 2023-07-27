package com.qulix.taskmanager.service;

import com.qulix.taskmanager.dto.EmployeeDto;
import com.qulix.taskmanager.exception.EntityValidationException;
import com.qulix.taskmanager.exception.RestRequestException;
import com.qulix.taskmanager.mapper.EmployeeMapper;
import com.qulix.taskmanager.model.Employee;
import com.qulix.taskmanager.model.Task;
import com.qulix.taskmanager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.qulix.taskmanager.enums.EmployeeStatus.DELETED;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    public final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeDto> getAllActiveEmployees() {
        List<Employee> employees = employeeRepository.findByStatusIsNot(DELETED);
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDto newEmployeeDto = new EmployeeDto();
            EmployeeMapper.updateEmployeeDto(employee, newEmployeeDto);
            employeeDtoList.add(newEmployeeDto);
        }
        return employeeDtoList;
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDto(savedEmployee);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        validateForDeletion(employeeDto.getId());

        Employee foundEmployee = getById(employeeDto.getId());
        EmployeeMapper.updateEmployee(employeeDto, foundEmployee);
        Employee employee = employeeRepository.save(foundEmployee);
        EmployeeDto newEmployeeDto = new EmployeeDto();
        EmployeeMapper.updateEmployeeDto(employee, newEmployeeDto);
        return newEmployeeDto;
    }

    @Override
    public void deleteEmployee(Integer id) {
        validateForDeletion(id);

        Employee employee = getById(id);
        employee.setStatus(DELETED);

        List<Employee> removeCandidates = new ArrayList<>();
        for (Task task : employee.getTasks()) {
            for (Employee assignedEmployee : task.getEmployees()) {
                if (Objects.equals(assignedEmployee.getId(), id)) {
                    removeCandidates.add(assignedEmployee);
                }
            }
            task.getEmployees().removeAll(removeCandidates);
        }

        employee.getTasks().clear();

        employeeRepository.save(employee);
    }

    private Employee getById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RestRequestException("Employee with id " + id + " not found"));
    }

    private void validateForDeletion(Integer id) {
        Employee employee = getById(id);
        if (employee.getStatus() == DELETED) {
            throw new EntityValidationException("Employee has deleted");
        }
    }
}
