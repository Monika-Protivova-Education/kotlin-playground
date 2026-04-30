-- Seed users (passwords are BCrypt-encoded: "password" and "admin")
INSERT INTO users (id, username, password, role) VALUES (1, 'user', '$2y$10$NQwrvqmOUjQBWO1mAS8Ju.Ympznnq1skcOvFEpJVUVycqy1uX91pa', 'USER') ON CONFLICT DO NOTHING;
INSERT INTO users (id, username, password, role) VALUES (2, 'admin', '$2y$10$/RxeBLDjZM1KaDGK2BAUwexrWm4GW9H3zDlRD8ewsQRkfvWDRBYgK', 'ADMIN') ON CONFLICT DO NOTHING;

-- Seed tasks
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 1 exercises', 'COMPLETED', 1) ON CONFLICT DO NOTHING;
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 2 exercises', 'COMPLETED', 1) ON CONFLICT DO NOTHING;
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 3 exercises', 'COMPLETED', 1) ON CONFLICT DO NOTHING;
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 4 exercises', 'COMPLETED', 1) ON CONFLICT DO NOTHING;
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 5 exercises', 'IN_PROGRESS', 2) ON CONFLICT DO NOTHING;
INSERT INTO tasks (description, status, created_by) VALUES ('Complete lesson 6 exercises', 'NOT_STARTED', 2) ON CONFLICT DO NOTHING;
