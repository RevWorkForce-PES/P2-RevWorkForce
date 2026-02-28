package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.Gender;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing an Employee in the RevWorkForce system.
 *
 * <p>This class maps to the EMPLOYEES database table and contains:
 * <ul>
 *     <li>Personal information</li>
 *     <li>Employment details</li>
 *     <li>Authentication & security details</li>
 *     <li>Audit tracking fields</li>
 *     <li>Role-based access mapping</li>
 * </ul>
 *
 * <p>This entity is central to:
 * <ul>
 *     <li>User authentication</li>
 *     <li>Authorization (role-based access)</li>
 *     <li>Organizational hierarchy</li>
 * </ul>
 *
 * <p>Table: EMPLOYEES
 *
 * @author RevWorkForce Team
 */

@Entity
@Table(name = "EMPLOYEES")
public class Employee {

    /**
     * Unique identifier for each employee.
     * 
     * Uses String instead of numeric ID to allow custom formats (e.g., EMP001).
     */
    @Id
    @Column(name = "employee_id", length = 20)
    private String employeeId;

    /**
     * Employee's first name.
     * Cannot be null.
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * Employee's last name.
     * Cannot be null.
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * Official company email.
     * Must be unique across system.
     * Used for login and communication.
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Contact phone number.
     */
    @Column(name = "phone", length = 15)
    private String phone;

    /**
     * Date of birth.
     * Used for HR records only.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Gender stored as STRING enum (not ordinal)
     * This prevents database issues if enum order changes.
     */ 
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    /**
     * Residential address details.
     */
    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    /**
     * Default country set to India.
     */
    @Column(name = "country", length = 50)
    private String country = "India";

    /**
     * Emergency contact details for safety compliance.
     */
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 15)
    private String emergencyContactPhone;

    /**
     * Many employees belong to one Department.
     * Lazy fetch to avoid unnecessary loading.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    
    /**
     * Many employees share one Designation.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designation_id")
    private Designation designation;
    /**
     * Self-referencing relationship.
     * Represents reporting hierarchy (Manager → Employee).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    /**
     * Employment dates.
     */
    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "leaving_date")
    private LocalDate leavingDate;

    /**
     * Employee salary.
     * Precision: 10 digits total, 2 decimal places.
     */
    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    /**
     * Current employment status.
     * Default = ACTIVE.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    /**
     * Hashed password for authentication.
     * Never store plain text passwords.
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * Indicates if employee is logging in for the first time.
     * Y = Yes, N = No.
     */
    @Column(name = "first_login", length = 1)
    private Character firstLogin = 'Y';

    /**
     * Last successful login timestamp.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * Number of consecutive failed login attempts.
     * Used for security lock mechanism.
     */
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    /**
     * Indicates whether account is locked.
     */
    @Column(name = "account_locked", length = 1)
    private Character accountLocked = 'N';

    /**
     * Timestamp until which account remains locked.
     */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    /**
     * Audit fields.
     * Automatically managed using lifecycle callbacks.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    /**
     * Many-to-Many relationship with Role.
     * 
     * EAGER fetch because roles are required immediately for authentication.
     * Cascade persist/merge allows automatic role association updates.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "EMPLOYEE_ROLES",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Default constructor.
     * Initializes timestamps and joining date.
     */
    public Employee() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.joiningDate = LocalDate.now();
    }

    /**
     * Convenience constructor for basic employee creation.
     */
    public Employee(String employeeId, String firstName, String lastName, String email) {
        this();
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Returns full name (FirstName + LastName).
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Adds role to employee.
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
     * Removes role from employee.
     */
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    /**
     * Checks if employee has specific role.
     * 
     * @param roleName role to check
     * @return true if assigned
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getRoleName().equals(roleName));
    }

    /**
     * Executed before entity is persisted.
     * Ensures timestamps and default joining date.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (joiningDate == null) {
            joiningDate = LocalDate.now();
        }
    }

    /**
     * Executed before entity update.
     * Updates modified timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Simplified string representation.
     * Excludes sensitive data like password.
     */
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public Character getAccountLocked() {
	    return accountLocked;
	}

	public EmployeeStatus getStatus() {
	    return status;
	}

	public String getPasswordHash() {
	    return passwordHash;
	}

	public Set<Role> getRoles() {
	    return roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public LocalDate getLeavingDate() {
		return leavingDate;
	}

	public void setLeavingDate(LocalDate leavingDate) {
		this.leavingDate = leavingDate;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Character getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Character firstLogin) {
		this.firstLogin = firstLogin;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public LocalDateTime getLockedUntil() {
		return lockedUntil;
	}

	public void setLockedUntil(LocalDateTime lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setAccountLocked(Character accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
}