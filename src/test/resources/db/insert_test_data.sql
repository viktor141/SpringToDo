-- Insert test data for integration tests
INSERT INTO todos (id, description, status, created_at, updated_at) VALUES 
(1, 'Test TODO 1', 'TODO', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'Test TODO 2', 'IN_PROGRESS', '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(3, 'Test TODO 3', 'DONE', '2024-01-01 12:00:00', '2024-01-01 12:00:00');
