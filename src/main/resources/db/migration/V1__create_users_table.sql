CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'USER',
    enabled INTEGER NOT NULL DEFAULT 1
);

INSERT INTO users (username, password, role) VALUES
    ('max', '$2a$10$ens3HaIDUUdSsvKObvq9AexcHWL.Kx62frHLmyDRMxyjYWuzm9/dq', 'ADMIN'),
    ('emily', '$2a$10$ens3HaIDUUdSsvKObvq9AexcHWL.Kx62frHLmyDRMxyjYWuzm9/dq', 'ADMIN');
