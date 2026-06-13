package com.ems.employeemanagement.dto;

import com.ems.employeemanagement.model.EmployeeStatus;
import com.ems.employeemanagement.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\-\\s]{7,15}$", message = "Phone number is invalid")
    private String phoneNumber;

    private String jobTitle;

    @NotNull(message = "Role is required")
    private Role role;

    private EmployeeStatus status;

    @NotNull(message = "Date of joining is required")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    private LocalDate dateOfJoining;

    @Positive(message = "Salary must be positive")
    private Double salary;

    private Long departmentId;

    private Long managerId;
}
