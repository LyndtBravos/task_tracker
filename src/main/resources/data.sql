INSERT INTO users (id, username, email, password, role) VALUES
(1, 'lindt', 'lindtbravos@gmail.com', 'MuchToLose#1', 'ADMIN'),
(2, 'future', 'futurexmamma@gmail.com', 'MuchToLose#2', 'USER');

INSERT INTO tasks (id, title, description, status, due_date, created_date, assigned_user_id) VALUES
(1, 'Laundry', 'Take stuff to the laundry place', 'NEW', '2025-06-02', '2025-06-11', 1),
(2, 'Clean', 'Scrub the shower', 'NEW', '2025-06-02', '2025-06-10', 2);
