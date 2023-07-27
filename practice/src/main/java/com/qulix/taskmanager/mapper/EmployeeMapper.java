package com.qulix.taskmanager.mapper;

import com.qulix.taskmanager.dto.EmployeeDto;
import com.qulix.taskmanager.model.Employee;

import java.util.ArrayList;
import java.util.List;

import static com.qulix.taskmanager.enums.EmployeeStatus.ACTIVE;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .lastName(employeeDto.getLastName())
                .firstName(employeeDto.getFirstName())
                .patronymic(employeeDto.getPatronymic())
                .post(employeeDto.getPost())
                .status(ACTIVE)
                .build();
    }

    public static EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .patronymic(employee.getPatronymic())
                .post(employee.getPost())
                .build();
    }

    public static void updateEmployeeDto(Employee employee, EmployeeDto employeeDtoToUpdate) {
        employeeDtoToUpdate.setId(employee.getId());
        employeeDtoToUpdate.setLastName(employee.getLastName());
        employeeDtoToUpdate.setFirstName(employee.getFirstName());
        employeeDtoToUpdate.setPatronymic(employee.getPatronymic());
        employeeDtoToUpdate.setPost(employee.getPost());
    }

    public static void updateEmployee(EmployeeDto employeeDto, Employee employeeToUpdate) {
        employeeToUpdate.setLastName(employeeDto.getLastName());
        employeeToUpdate.setFirstName(employeeDto.getFirstName());
        employeeToUpdate.setPatronymic(employeeDto.getPatronymic());
        employeeToUpdate.setPost(employeeDto.getPost());
    }

    public static List<Employee> toEntityList(List<EmployeeDto> employeeDtoList) {
        List<Employee> employeeList = new ArrayList<>();
        for (EmployeeDto employeeDto : employeeDtoList) {
            Employee newEmployee = new Employee();
            newEmployee.setId(employeeDto.getId());
            updateEmployee(employeeDto, newEmployee);
            employeeList.add(newEmployee);
        }
        return employeeList;
    }

    public static List<EmployeeDto> toDtoList(List<Employee> employeeList) {
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeeDto newEmployeeDto = new EmployeeDto();
            updateEmployeeDto(employee, newEmployeeDto);
            employeeDtoList.add(newEmployeeDto);
        }
        return employeeDtoList;
    }
}
