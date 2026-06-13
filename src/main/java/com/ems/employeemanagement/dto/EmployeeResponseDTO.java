package com.ems.employeemanagement.dto;

import com.ems.employeemanagement.model.EmployeeStatus;
import com.ems.employeemanagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String jobTitle;
    private Role role;
    private EmployeeStatus status;
    private LocalDate dateOfJoining;
    private Double salary;
    private Long departmentId;
    private String departmentName;
    private Long managerId;
    private String managerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
