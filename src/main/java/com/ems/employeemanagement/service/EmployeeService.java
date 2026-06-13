package com.ems.employeemanagement.service;

import com.ems.employeemanagement.dto.EmployeeRequestDTO;
import com.ems.employeemanagement.dto.EmployeeResponseDTO;
import com.ems.employeemanagement.dto.RoleUpdateRequest;
import com.ems.employeemanagement.exception.DuplicateResourceException;
import com.ems.employeemanagement.exception.ResourceNotFoundException;
import com.ems.employeemanagement.model.Department;
import com.ems.employeemanagement.model.Employee;
import com.ems.employeemanagement.model.EmployeeStatus;
import com.ems.employeemanagement.model.Role;
import com.ems.employeemanagement.repository.DepartmentRepository;
import com.ems.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeResponseDTO onboardEmployee(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + request.getEmail() + " already exists");
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .jobTitle(request.getJobTitle())
                .role(request.getRole())
                .status(request.getStatus() != null ? request.getStatus() : EmployeeStatus.ACTIVE)
                .dateOfJoining(request.getDateOfJoining())
                .salary(request.getSalary())
                .build();

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> ResourceNotFoundException.forEntity("Department", request.getDepartmentId()));
            employee.setDepartment(department);
        }

        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> ResourceNotFoundException.forEntity("Manager", request.getManagerId()));
            employee.setManager(manager);
        }

        Employee saved = employeeRepository.save(employee);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Employee", id));
        return toResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Employee", id));

        if (!employee.getEmail().equalsIgnoreCase(request.getEmail())
                && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + request.getEmail() + " already exists");
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setJobTitle(request.getJobTitle());
        employee.setRole(request.getRole());
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }
        employee.setDateOfJoining(request.getDateOfJoining());
        employee.setSalary(request.getSalary());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> ResourceNotFoundException.forEntity("Department", request.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        if (request.getManagerId() != null) {
            if (request.getManagerId().equals(id)) {
                throw new IllegalArgumentException("An employee cannot be their own manager");
            }
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> ResourceNotFoundException.forEntity("Manager", request.getManagerId()));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        Employee updated = employeeRepository.save(employee);
        return toResponseDTO(updated);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployeeRole(Long id, RoleUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Employee", id));
        employee.setRole(request.getRole());
        return toResponseDTO(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponseDTO updateEmployeeStatus(Long id, EmployeeStatus status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Employee", id));
        employee.setStatus(status);
        return toResponseDTO(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEntity("Employee", id));
        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> searchEmployees(String keyword) {
        return employeeRepository.searchByKeyword(keyword)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getDirectReports(Long managerId) {
        return employeeRepository.findByManagerId(managerId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private EmployeeResponseDTO toResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .jobTitle(employee.getJobTitle())
                .role(employee.getRole())
                .status(employee.getStatus())
                .dateOfJoining(employee.getDateOfJoining())
                .salary(employee.getSalary())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .managerId(employee.getManager() != null ? employee.getManager().getId() : null)
                .managerName(employee.getManager() != null
                        ? employee.getManager().getFirstName() + " " + employee.getManager().getLastName()
                        : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
