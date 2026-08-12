-- Seed Categories
INSERT INTO categories (category_id, code, name, description)
VALUES
    (1, 'LAPTOP', 'Laptops', 'Company issued laptops'),
    (2, 'PERIPHERAL', 'Peripherals', 'Monitors, keyboards, and mice');

-- Seed Employees
INSERT INTO employees (employee_id, first_name, last_name, email, department, status)
VALUES
    ('EMP-001', 'Alex', 'Mercer', 'alex.mercer@company.com', 'Engineering', 'ACTIVE'),
    ('EMP-002', 'Sarah', 'Connor', 'sarah.connor@company.com', 'IT', 'ACTIVE');

-- Seed Assets
INSERT INTO assets (asset_tag, name, serial_number, status, purchase_cost, purchase_date, category_id, assigned_employee_id)
VALUES
    ('AST-1001', 'MacBook Pro 16"', 'SN-MBP-9981', 'AVAILABLE', 2499.00, '2026-01-15', 1, NULL),
    ('AST-1002', 'Dell XPS 15', 'SN-XPS-4432', 'ASSIGNED', 1899.50, '2026-02-01', 1, 1),
    ('AST-1003', 'Dell 27" 4K Monitor', 'SN-MON-7711', 'AVAILABLE', 450.00, '2026-03-10', 2, NULL);