package com.ems.employeemanagement.controller;

import com.ems.employeemanagement.dto.ApiResponse;
import com.ems.employeemanagement.dto.EmployeeRequestDTO;
import com.ems.employeemanagement.dto.EmployeeResponseDTO;
import com.ems.employeemanagement.dto.RoleUpdateRequest;
import com.ems.employeemanagement.model.EmployeeStatus;
import com.ems.employeemanagement.model.Role;
import com.ems.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Onboarding
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> onboardEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {
        EmployeeResponseDTO created = employeeService.onboardEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee onboarded successfully", created));
    }

    // Record maintenance - read
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getAllEmployees() {
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employeeService.getAllEmployees()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employeeService.getEmployeeById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> searchEmployees(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success("Search results", employeeService.searchEmployees(keyword)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employeeService.getEmployeesByDepartment(departmentId)));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getEmployeesByRole(@PathVariable Role role) {
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employeeService.getEmployeesByRole(role)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employeeService.getEmployeesByStatus(status)));
    }

    @GetMapping("/{managerId}/direct-reports")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getDirectReports(@PathVariable Long managerId) {
        return ResponseEntity.ok(ApiResponse.success("Direct reports retrieved successfully", employeeService.getDirectReports(managerId)));
    }

    // Record maintenance - update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", employeeService.updateEmployee(id, request)));
    }

    // Role management
    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployeeRole(
            @PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Employee role updated successfully", employeeService.updateEmployeeRole(id, request)));
    }

    // Status management (onboarding/offboarding lifecycle)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployeeStatus(
            @PathVariable Long id, @RequestParam EmployeeStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Employee status updated successfully", employeeService.updateEmployeeStatus(id, status)));
    }

    // Record maintenance - delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }
}
