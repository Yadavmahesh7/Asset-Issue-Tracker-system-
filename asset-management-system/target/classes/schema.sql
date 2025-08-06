-- Create Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255)
);

-- Create User Roles Join Table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Create Assets Table
CREATE TABLE IF NOT EXISTS assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    asset_type VARCHAR(255) NOT NULL,
    serial_number VARCHAR(255) UNIQUE,
    status VARCHAR(255) NOT NULL -- e.g., AVAILABLE, ALLOCATED, MAINTENANCE
);

-- Create Asset Requests Table
CREATE TABLE IF NOT EXISTS asset_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    asset_type VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    justification TEXT,
    status VARCHAR(255) NOT NULL, -- e.g., PENDING, APPROVED, REJECTED
    request_date TIMESTAMP NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES users(id)
);

-- Create Asset Allocations Table
CREATE TABLE IF NOT EXISTS asset_allocations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    issue_date TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    return_status BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (employee_id) REFERENCES users(id)
);

-- Create Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(255) NOT NULL,
    performed_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    details TEXT
);

-- Pre-populate roles
INSERT INTO roles (name) VALUES ('EMPLOYEE'), ('ADMIN'), ('STORE') ON DUPLICATE KEY UPDATE name=name;
