-- Setup Vehicle Types
INSERT INTO car_types (car_type_id, vehicle_type) VALUES 
(1, 'SEDAN'),
(2, 'SUV'),
(3, 'VAN');
-- Setup Car Fleet
-- INSERT INTO cars (car_id, car_type_id, status) VALUES
-- (1, 1, 'AVAILABLE'),
-- (2, 2, 'RESERVED'),
-- (3, 1, 'AVAILABLE'),
-- (4, 2, 'RESERVED');
-- Setup Users
-- INSERT INTO users (user_id, first_name, last_name, username, password) VALUES
-- (1, 'John', 'Doe', 'johndoe', 'password123'),
-- (2, 'Jane', 'Smith', 'janesmith', 'securepass456'),
-- (3, 'Alice', 'Johnson', 'alicej', 'mypassword789'),
-- (4, 'Bob', 'Brown', 'bobbrown', 'pass456');
-- Setup Reservations
-- INSERT INTO reservations (reservation_id, user_id, car_id, start_date, end_date, status) VALUES
-- (1, 1, 1, '2024-10-01 10:00:00', '2024-10-03 10:00:00', 'ACTIVE'),
-- (2, 2, 2, '2024-10-02 11:00:00', '2024-10-05 11:00:00', 'CANCELLED'),
-- (3, 3, 1, '2024-10-04 09:00:00', '2024-10-06 09:00:00', 'COMPLETED'),
-- (4, 4, 2, '2024-10-07 15:00:00', '2024-10-07 16:00:00', 'ACTIVE');