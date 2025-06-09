CREATE TABLE person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    gender VARCHAR(15) NOT NULL
);

INSERT INTO person (first_name, last_name, email, address, gender)
VALUES 
  ('Alice', 'Johnson', 'alice.johnson@example.com', '123 Maple Street', 'Female'),
  ('Bob', 'Smith', 'bob.smith@example.com', '456 Oak Avenue', 'Male'),
  ('Clara', 'Brown', 'clara.brown@example.com', '789 Pine Road', 'Female'),
  ('David', 'Lee', 'david.lee@example.com', '101 Birch Blvd', 'Male'),
  ('Eva', 'Martins', 'eva.martins@example.com', '202 Cedar Lane', 'Female');
