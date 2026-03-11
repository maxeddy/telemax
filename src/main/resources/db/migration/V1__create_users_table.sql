CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'USER',
    enabled INTEGER NOT NULL DEFAULT 1
);

INSERT INTO users (username, password, role) VALUES
    ('max', '$2a$10$d9.Cz8wmPGNbg.61nUfrzeM9u.1wbF/c7yWaYh.1YEBUyDhpYN3j6', 'ADMIN'),
    ('emily', '$2a$10$9QI6KsLA9nesYsS2mRWDxu83Kvz4jZI7LfbyteEbc.PHct0IWeEt.', 'ADMIN'),
    ('casper', '$2a$10$jJX3POeJka9ki1hlvcOGtulrTMQkjZU8jtoKsAlJCRbqLvNEYd8qi', 'ADMIN');
