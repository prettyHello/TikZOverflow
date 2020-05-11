CREATE TABLE IF NOT EXISTS users (
                                     user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     first_name TEXT NOT NULL,
                                     last_name TEXT NOT NULL,
                                     email TEXT NOT NULL UNIQUE,
                                     phone TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL,
                                     salt TEXT NOT NULL UNIQUE,
                                     register_date TEXT NOT NULL

);


CREATE TABLE IF NOT EXISTS  projects (
                                         project_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         project_owner_id INTEGER NOT NULL,
                                         name TEXT NOT NULL,
                                         path TEXT NOT NULL UNIQUE,
                                         creation_date TEXT NOT NULL,
                                         modification_date TEXT NOT NULL,
                                         hash TEXT,
                                         FOREIGN KEY (project_owner_id)
                                                 REFERENCES users (user_id)
                                                 ON DELETE CASCADE
                                                 ON UPDATE NO ACTION

);