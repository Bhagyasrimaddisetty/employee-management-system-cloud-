package com.ems.employeemanagement;

import com.ems.employeemanagement.dto.EmployeeRequestDTO;
import com.ems.employeemanagement.dto.EmployeeResponseDTO;
import com.ems.employeemanagement.model.Role;
import com.ems.employeemanagement.repository.DepartmentRepository;
import com.ems.employeemanagement.repository.EmployeeRepository;
import com.ems.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void onboardEmployee_shouldCreateEmployeeSuccessfully() {
        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("+1234567890")
                .jobTitle("Software Engineer")
                .role(Role.EMPLOYEE)
                .dateOfJoining(LocalDate.now())
                .salary(75000.0)
                .build();

        EmployeeResponseDTO response = employeeService.onboardEmployee(request);

        assertNotNull(response.getId());
        assertEquals("Jane", response.getFirstName());
        assertEquals("jane.doe@example.com", response.getEmail());
        assertEquals(Role.EMPLOYEE, response.getRole());
    }

    @Test
    void getEmployeeById_shouldThrowWhenNotFound() {
        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(999L));
    }

    @Test
    void onboardEmployee_shouldThrowOnDuplicateEmail() {
        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@example.com")
                .role(Role.EMPLOYEE)
                .dateOfJoining(LocalDate.now())
                .build();

        employeeService.onboardEmployee(request);

        assertThrows(RuntimeException.class, () -> employeeService.onboardEmployee(request));
    }
}
